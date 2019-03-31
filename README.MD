# temp: template project

[![Build Status](https://travis-ci.org/WebGoat/WebGoat.svg?branch=develop)](https://travis-ci.org/WebGoat/WebGoat)
[![Coverage Status](https://coveralls.io/repos/WebGoat/WebGoat/badge.svg?branch=develop&service=github)](https://coveralls.io/github/WebGoat/WebGoat?branch=master)
[![GitHub release](https://img.shields.io/github/release/WebGoat/WebGoat.svg)](https://github.com/WebGoat/WebGoat/releases/latest) 

# Run Instructions:

## spring-boot style
generate a jar and deploy it in embed web container, at most case, it is run in docker container

## legacy spring style
generate a war and deploy it in a web container.

1.XML Configuration

We sow beans in xml. We allocate the property of beans in xml. We manage the lifecycle of beans in xml.

2.XML Configuration with namespace

We distribute beans in different namespace, such as Spring pre-defined namespaces: mvc, context, aop, tx, cache, etc.
Or any other qualified(scheme's handler implements NamespaceHandler), like dubbo, rabbit.

3.Annotation driven

We can use annotation to bind property, inject value, and control the lifecycle at same time!

4.Java Config

We use java code breed beans.