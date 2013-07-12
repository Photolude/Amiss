Amiss
=====

This is a simple endpoint monitoring service. It allows for a list of endpoint urls to be called on a specific duration, and will notify via email as to any status changes to those urls. It also comes with a basic web interface which runs on tomcat 7, which allows you to easily monitor the current status of the urls provided.

When the service starts up it sends an email to all those who are subscribed via the config file.  This email contains all the services being monitored and their initial state.  Once the service is live it will only send email when a url changes state either to healthy or unhealthy.
