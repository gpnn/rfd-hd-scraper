# Red Flag Deals Hot Deals Scraper

## Note

**I started this project when I was on my first internship, I have learned a lot since then and I have decided to rewrite this project (in Go)! This project will no longer be maintained and the replacement is located here: <https://github.com/gordonpn/hot-flag-deals>.**

---

## Description

This project aims to scrape the content of the [Hot Deals forums](http://forums.redflagdeals.com/hot-deals-f9/), keep track of all interesting and relevant deals, as well as archive all other deals. All relevant deals are emailed daily to a mailing list and then archived.
There also exists a front-end at <https://gordon-pn.com/deals> to view the current relevant deals.

## Motivation

Red Flag Deals does aggregate deals on their front page, but the Hot Deals **Forums** are community driven and sourced by anybody. This is where the purpose of my project comes into play, this project scrapes the Hot Deals Forums several times per day and displays them on a front-end.

With this project, I saved myself the chore of checking the (messy) forum a few times a day while still being aware of the good deals posted by the community.

---

![GitHub](https://badgen.net/github/license/gordonpn/rfd-hd-scraper)
[![Project Status: Moved to https://github.com/gordonpn/hot-flag-deals](https://www.repostatus.org/badges/latest/moved.svg)](https://www.repostatus.org/#moved) to [github.com/gordonpn/hot-flag-deals](https://github.com/gordonpn/hot-flag-deals)

## Screenshots

### Daily email

<a href="./docs/daily-email.png"><img src="./docs/daily-email.png" height="600"></a>

Template made by @tiffzeng

## Technologies

* Maven: Dependency management
* Bootstrap: CSS framework for front-end
* jQuery: front-end
* Javalin: Web framework for Java for the back-end
* Spring Framework: Utilized Thymeleaf for email templates as well as some dependency injection
* jsoup: library to parse HTML documents

## Prerequisites

* Java 8+
* Apache Maven 3.6+

## Installation

Clone the master branch into your workspace.

Compile and package using Maven.

```bash
mvn clean compile package
```

## Configuration

Edit the configuration.json to your needs.
You must set your gmail and password as environment variables.
In my case, my prod machine was running on Linux and my test machines were running on Mac and Windows.
Those settings come from the ConfigurationLoader.java.

## Usage

The main class `com.rfdhd.scraper.App` is used for scraping the forum.

```bash
java -cp *.jar com.rfdhd.scraper.App
```

The main class `com.rfdhd.scraper.DigestCreator` is used for sending the daily digest email. It will take the content of dailyDigest.json as source.

```bash
java -cp *.jar com.rfdhd.scraper.DigestCreator
```

The main class `com.rfdhd.scraper.Start` is used to start the back-end to respond to the HTTP requests.

```bash
java -cp *.jar com.rfdhd.scraper.Start
```

## Use case

The Scraper and the DigestCreator are both automated in Jenkins in order to have the most up to date information on deals.

## Roadmap/Todo

### Phase 1

* [x]  Use the Jsoup library to scrape data correctly.
* [x]  Save all the scraped data in a map.
* [x]  Save the unfiltered map into scrapings.json
* [x]  Try to read scrapings.json
* [x]  Remove duplicates before saving again
* [x]  Utility class to calculate information from a map.
* [x]  Filter the raw map using the utility class.
* [x]  Save the filtered map into currentLinks.json

### Phase 2

* [x]  Parse direct link to product
* [x]  Create a configuration file in resources with the property of pages to scrape.
* [x]  Spring framework beans for configuration
* [x]  Refactor currentLinks to dailyDigest
* [x]  Refactor pastLinks to archive
* [x]  Add mailing list to configuration
* [x]  Add email settings to configuration
* [x]  When filtering for dailyDigest, read scrapings and get median votes count of all
* [x]  When scrapings are filtered, it must check with archive if an item has already been processed
* [x]  When email service reads from dailyDigest, move items to archive
* [x]  Set up Mail service
* [x]  Set up Thymeleaf engine
* [x]  Environment variables getter
* [x]  Implement Spring framework beans

### Phase 3

* [x]  Add content body under h2
* [x]  Add a good readme.md
* [x]  Sort descending by votes before sending email
* [x]  Record thread start time
* [x]  Parse post date
* [x]  Keep the most recent version of the scraped posts
* [x]  Fix logic with scrapings (threads not going to dailyDigest if it was previously scraped with a low vote score (because it was they wre already in scrapings))
  * To fix these two issues:
    * [x]  Make use of LinkedHashMap to preserve the order of insertion.
    * [x]  Try to read the existing files before scraping. And put into those existing maps, thus updating values with identical keys.
    * [x]  Only filter based on the median of pages scraped, not entire scrapings json.
    * [x]  Save the interesting threads in dailyDigest disregarding the duplicates found in scrapings.
* [x]  When preparing the email, remove the duplicates by comparing with archive.
* [x]  Filter out threads older than 72 hours when preparing email.
* [x]  Read from config and template within the jar

### Phase 4

* [ ]  Add command line flags to differentiate testing on prod and prod
* [x]  A front-end
* [ ]  Finish implementing back-end for signing up
* [ ]  Refactor how the configurations are acquired.

### Phase 5

* [ ]  Write tests
* [ ]  Improve styling of email template
* [ ]  Integrate MongoDB or lowdb for a database

## License

[MIT License](./LICENSE)
