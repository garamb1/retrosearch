# RetroSearch
### Spring-based Web Search App for old computers

Search and Browse the WWW like it's 1997.

[![Build & Publish on DockerHub](https://github.com/garamb1/retrosearch/actions/workflows/publish.yml/badge.svg)](https://github.com/garamb1/retrosearch/actions/workflows/publish.yml)

### Check it out at: [retrosearch.org](http://retrosearch.org)
### Self-host with [Docker Image](https://hub.docker.com/repository/docker/garambo/retrosearch)

![RetroSearch.org on Netscape 2.0 and IE 5](https://github.com/garamb1/retrosearch/assets/3776646/c72e5fa5-dee8-4ea5-b2f4-211baafae626)


## How does it work?

RetroSearch is a Spring Web Application that presents very simple HTML pages which can be interpreted by old browsers.
It provides the ability to search the Web using DuckDuckGo with a custom scraper that loads the first page of results and allows you to browse pages in plain text.
You can deploy it on your local network and access it from your old computer!

### Enabling the News API

RetroSearch can fetch news articles by using the GNews API, to allow this, add the environment variables as follows when running the Docker image:

```
docker run -e NEWS_ACTIVE=true -e NEWS_API_KEY={your GNews API Key} -d -p80:8080 garambo/retrosearch:{Retro Search Version} --restart unless-stopped
```

If running locally, just replace the property values in `application.properties` or create a new Spring run configuration.

### WIP
Currently in progress:
 - Improve the parsing abilities for the browsing functionality
 - Add documentation and automated tests
