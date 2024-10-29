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

    opens com.syengo.sufeeds.sufeeds to javafx.fxml;
    exports com.syengo.sufeeds.sufeeds;
    opens com.syengo.sufeeds.sufeeds.ui to javafx.fxml;
    opens com.syengo.sufeeds.sufeeds.models to javafx.base;
    exports com.syengo.sufeeds.sufeeds.ui;
    exports com.syengo.sufeeds.sufeeds.models;
    exports com.syengo.sufeeds.sufeeds.dao;
    exports com.syengo.sufeeds.sufeeds.config;
    opens com.syengo.sufeeds.sufeeds.main to javafx.graphics, javafx.fxml;
    exports com.syengo.sufeeds.sufeeds.main;
}
