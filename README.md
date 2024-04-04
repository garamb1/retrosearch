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

## News and sports APIs support

RetroSearch can fetch news articles by using the GNews API and the latest football scores using the football-data.org API, to allow this, add the environment variables as follows when running the Docker image:


### Enabling the News API

```
docker run -e NEWS_ACTIVE=true -e NEWS_API_KEY={your GNews API Key} -d -p80:8080 garambo/retrosearch:{Retro Search Version} --restart unless-stopped
```

### Enabling the football-data.org API

```
docker run -e FOOTBALL_API_ACTIVE=true -e FOOTBALL_API_KEY={your football-data.org API Key} -d -p80:8080 garambo/retrosearch:{Retro Search Version} --restart unless-stopped
```

The additional `FOOTBALL_API_RESULTS_PAST_DAYS` variable can be used to determine how many past days the scores can be fetched, `3` by default.

### Enabling both

```
docker run -e NEWS_ACTIVE=true -e NEWS_API_KEY={your GNews API Key} -e FOOTBALL_API_ACTIVE=true -e FOOTBALL_API_KEY={your football-data.org API Key} -d -p80:8080 garambo/retrosearch:{Retro Search Version} --restart unless-stopped
```

If running locally, just replace the property values in `application.properties` or create a new Spring run configuration.


### WIP
Currently in progress:
 - Improve the parsing abilities for the browsing functionality
 - Add documentation and automated tests
