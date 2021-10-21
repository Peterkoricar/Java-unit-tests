package com.example.demo.widget.repository;


import com.example.demo.widget.model.Widget;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@ExtendWith(DBUnitExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class WidgetRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private WidgetRepository repository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DataSet("widgets.yml")
    void testFindAll() {
        // call findAll method
        List<Widget> widgets = Lists.newArrayList(repository.findAll());
        // assert that correct number of widgets was returned based on the DataSet
        Assertions.assertEquals(2, widgets.size(), "Expected 2 widgets in the database");
    }

    @Test
    @DataSet("widgets.yml")
    void testFindByIdSuccess() {
        //  call findById method
        Optional<Widget> widgets = repository.findById(1l);
        // use assertEquals on all Widget properties
        Assertions.assertEquals("Widget 1", widgets.get().getName());
        Assertions.assertEquals("This is widget 1", widgets.get().getDescription());

    }

    @Test
    @DataSet("widgets.yml")
    void testFindByIdNotFound() {
        // test findById for ID that is not present in widgets.yml dataset and assert that it is not found
        Optional<Widget> widgets = repository.findById(3l);
        Assertions.assertEquals(false, widgets.isPresent());
    }

    @Test
    @DataSet("widgets.yml")
    void testFindWidgetsWithNameLike() {
        // test method findWidgetsWithNameLike that it returns correct number of widgets
        List<Widget> widgets = repository.findWidgetsWithNameLike("Widget%");
        Assertions.assertEquals(2, widgets.size());

    }
}
