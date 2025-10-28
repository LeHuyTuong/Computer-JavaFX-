module sum25.se196853.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Spring Boot & Core
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.boot;
    requires spring.beans;
    requires spring.core;
    requires spring.aop;
    requires spring.web;

    // Spring Data JPA & Persistence
    requires spring.data.jpa;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires spring.data.commons;
    requires spring.tx;

    // JDBC Driver
    requires java.sql;
    requires jakarta.transaction;

    // Validation & Injection APIs
    requires jakarta.validation;
    requires jakarta.inject;
    requires jakarta.annotation;
    requires jakarta.cdi;


    opens sum25.se196853.demo to javafx.graphics, javafx.fxml, spring.core, spring.beans, spring.context;
    opens sum25.se196853.demo.controller to javafx.fxml, spring.core, spring.beans, spring.context, javafx.weaver.spring;
    opens sum25.se196853.demo.entity to org.hibernate.orm.core, spring.core, javafx.base;
    opens sum25.se196853.demo.service to spring.core, spring.beans, spring.context;
    opens sum25.se196853.demo.service.impl to spring.core, spring.beans, spring.context;
    opens sum25.se196853.demo.repository to spring.core, spring.beans, spring.context, spring.data.jpa;
    opens sum25.se196853.demo.config to spring.core, spring.beans, spring.context;


    exports sum25.se196853.demo;
    exports sum25.se196853.demo.controller;
    exports sum25.se196853.demo.entity;
    exports sum25.se196853.demo.service;
    exports sum25.se196853.demo.repository;
}
