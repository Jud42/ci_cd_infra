#!/bin/sh

COMPOSE_DIR="./docker_services/compose.yml"
INFO_INFRA="info_infra.txt"
token=""

print_help() {
    echo "Usage: $0 [OPTION]"
    echo
    echo "Options:"
    echo "  --create        Start Docker containers and record their IP addresses."
    echo "  --destroy       Stop Docker containers and remove the info_infra.txt file."
    echo "  --destroy-all   Stop Docker containers, remove all images and volumes, and delete the info_infra.txt file."
    echo "  --help          Display this help message."
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


    sleep 10 && echo "jenkins-initial-password=$(docker exec jenkins-ansible-container \
        cat /var/jenkins_home/secrets/initialAdminPassword \
        2>/dev/null)" >> $INFO_INFRA

    echo "--------info_infra.txt--------"
    awk '{print "   " $0}' $INFO_INFRA
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
