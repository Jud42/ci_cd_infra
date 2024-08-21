#!/bin/sh

COMPOSE_DIR="./docker_services/compose.yml"
INVENTORY_PATH="ansible/00_inventory.yml"
INFO_INFRA="info_containers.txt"
NGINX_CONTAINER="nginx-container"
WEBSERV_CONTAINER="webserv-container"
JENKINS_URL="http://localhost:8082"

token=""

print_help() {
    echo "Usage: $0 [OPTION]"
    echo
    echo "Options:"
    echo "  --create        Start Docker containers and record their IP addresses."
    echo "  --destroy       Stop Docker containers and remove the info_containers.txt file."
    echo "  --destroy-all   Stop Docker containers, remove all images and volumes, and delete the info_containers.txt file."
    echo "  --get-pass	    This option retrieves the initial administrator password for Jenkins."
    echo "  --help          Display this help message."
}

generator_inventory() {

    nginx_ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $NGINX_CONTAINER)
    webserv_ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $WEBSERV_CONTAINER)

    cat << EOF > $INVENTORY_PATH
    all:
      children:
      
        #serveur RHEL
        servers:
          children:
            nginx:
              hosts:
                ${nginx_ip}:
                
            webserver:
              hosts:
                ${webserv_ip}:
  
      vars:
        #ansible_python_interpreter: /usr/bin/python3
        ansible_ssh_pass: "rootpass"
        nginx_ip: ${nginx_ip}
        webserv_ip: ${webserv_ip}
EOF
}

run_() {
    
    if [ -f $INFO_INFRA ]; then
        rm $INFO_INFRA
    fi
    docker compose -f $COMPOSE_DIR up -d && \
    for container in $(docker ps --format '{{.Names}}'); do
        ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $container)
        echo "$container=$ip" | tr -d ' ' >> $INFO_INFRA
    done

    # Allow Jenkins user to write to the directory where artifacts will be stored
    docker exec -u root jenkins-ansible-container chmod 777 \
        /var/jenkins_home/ansible/roles/tomcat/files
    
    # create inventory file
    generator_inventory
   
    echo "--------info_infra.txt--------"
    awk '{print "   " $0}' $INFO_INFRA

    echo "\n$JENKINS_URL"
}

destroy_() {
    docker compose -f $COMPOSE_DIR down --remove-orphans
    if [ -f $INFO_INFRA ]; then
        rm $INFO_INFRA
    fi
}

destroy_all_() {
    docker compose -f $COMPOSE_DIR down --rmi all --volumes --remove-orphans
    if [ -f $INFO_INFRA ]; then
        rm $INFO_INFRA
    fi
}

get_jenkinsInitialPass_() {

	pass=$(docker exec jenkins-ansible-container \
		cat /var/jenkins_home/secrets/initialAdminPassword 2>/dev/null)
	if [ "$pass" = "" ]; then
		echo "The password has already been initialized or the container is not ready!"
	else
		echo "$pass"
	fi
}

if [ $# -eq 1 ]; then
    case "$1" in 
        "--create")
            run_
            ;;
        "--destroy")
            destroy_
            ;;
        "--destroy-all")
            destroy_all_
            ;;
	"--get-pass")
	    get_jenkinsInitialPass_
	    ;;
        "--help")
            print_help
            ;;
        *)
            echo "Invalid argument. Use --help for usage information."
            ;;
    esac
else
    echo "Invalid argument count. Use --help for usage information."
fi
