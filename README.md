E-Commerce Development

The application has been developed following a microservices architecture, with each service running inside Docker containers and orchestrated using Docker Compose.
External requests are handled through an Nginx reverse proxy, which routes the traffic to the internal services.
The entire solution has been deployed on a DigitalOcean virtual machine, serving as the cloud infrastructure (IaaS).
Additionally, to assess the cloud security posture, the CSA CCM v4 framework is applied in combination with the NIST Cybersecurity Framewor
