FROM    redhat/ubi8

RUN yum -y install openssh-server openssh-clients vim net-tools python3
RUN ssh-keygen -A
RUN echo "root:rootpass" | chpasswd

CMD ["/usr/sbin/sshd", "-D"]