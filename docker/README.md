# WSL Docker setup

```
curl -fsSL https://get.docker.com -o get-docker.sh
```

```
sudo sh ./get-docker.sh
```

```
sudo usermod -aG docker $USER
```

```
newgrp docker
```

```
docker run hello-world
```

# test setup for docker compose, kafka

create directory in c drive, c:\training


download docker-compose.yml and save it on c:\training  directory

Launch Ubuntu / Win key (Ubuntu app)

on Linux prompt,

```
cd /mnt/c/training
```

```
ls 
```

ensure docker-compose.yml

use up to start the cluster

```
docker compose up
```

Use Ctrl + C to stop it


## Update hosts file to map DNS

Open CMD in Admin mode, type cmd on start, right click,  run as Administrator

```
notepad C:\Windows\System32\drivers\etc\hosts
```

paste this one line inside hosts file, do not modify  other settings

```
127.0.0.1 broker
```


# Docker bash

Open Ubuntu in WSL


```
cd /mnt/c/training
```


```
docker compose exec  broker bash
```

```
ls /var/lib/kafka/data
```

list topic-partitions



