#!/usr/bin/env python3

import json
import yaml
# File containing servers info
SERVERS_FILE = 'info_infra.txt'

def load_servers():
    inventory = {
        'all': {
            'children': {
                'servers': {
                    'children': {
                        'nginx': {
                            'hosts': {}
                        },
                        'webserver': {
                            'hosts': {}
                        }
                    }
                }
            },
            'vars': {
                'ansible_ssh_pass': 'rootpass',
                'nginx_ip': {},
                'webserv_ip': {}
            }
        }
    }

    with open(SERVERS_FILE, 'r') as file:
        for line in file:
            line = line.strip()
            if line and not line.startswith('#'):
                name, ip = line.split('=')
                ip = ip.strip()
                name = name.strip()

                # Add hosts
                if name.startswith('nginx-container'):
                    inventory['all']['children']['servers']['children']['nginx']['hosts'][ip] = {}
                    inventory['all']['vars']['nginx_ip'] = ip

                elif name.startswith('webserv-container'):
                    inventory['all']['children']['servers']['children']['webserver']['hosts'][ip] = {}
                    inventory['all']['vars']['webserv_ip'] = ip

    return inventory

if __name__ == '__main__':
    inventory = load_servers()
    #inventory = json.dumps(inventory, indent=2)
    #print(json.dumps(inventory, indent=2))
    yaml_output = yaml.dump(inventory, default_flow_style=False, sort_keys=False)

    print(yaml_output)
