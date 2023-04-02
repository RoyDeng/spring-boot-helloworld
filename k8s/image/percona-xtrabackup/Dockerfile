FROM ubuntu:20.04

RUN apt-get update && apt-get dist-upgrade -y && apt install -y wget gnupg2 curl lsb-release ncat

RUN wget https://repo.percona.com/apt/percona-release_latest.generic_all.deb
RUN dpkg -i percona-release_latest.generic_all.deb
RUN percona-release enable-only tools release
RUN apt-get update
RUN apt-get install -y mysql-client
RUN apt-get install -y percona-xtrabackup-80

CMD ["/bin/bash"]