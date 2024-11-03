module com.syengo.sufeeds.sufeeds {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires javafx.baseEmpty;
    requires javafx.graphics;


    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.postgresql.jdbc;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.commons;

    opens com.syengo.sufeeds.sufeeds to javafx.fxml;
    exports com.syengo.sufeeds.sufeeds;
    opens com.syengo.sufeeds.sufeeds.models to javafx.base;

    exports com.syengo.sufeeds.sufeeds.models;
    exports com.syengo.sufeeds.sufeeds.dao;
    exports com.syengo.sufeeds.sufeeds.config;
    exports com.syengo.sufeeds.sufeeds.ui.controllers to javafx.fxml;
    // Also export your main package
    exports com.syengo.sufeeds.sufeeds.main;

    // If you're using @FXML annotations, you also need to open the package
    opens com.syengo.sufeeds.sufeeds.ui.controllers to javafx.fxml;
}
