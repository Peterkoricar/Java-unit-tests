package com.example.demo.widget.service;

import com.example.demo.widget.model.Widget;
import com.example.demo.widget.repository.WidgetRepository;
import com.github.database.rider.core.api.dataset.DataSet;
import net.bytebuddy.implementation.auxiliary.MethodCallProxy;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class WidgetServiceTest {
    /**
     * Autowire in the service we want to test
     */
    @Autowired
    private WidgetService service;

    /**
     * Create a mock implementation of the WidgetRepository
     */
    @MockBean
    private WidgetRepository repository;

    @Test
    @DisplayName("Test findById Success")
    void testFindById() {
        // Setup our mock repository
        Widget widget = new Widget(1l, "Widget Name", "Description", 1);
        doReturn(Optional.of(widget)).when(repository).findById(1l);

        // Execute the service call
        Optional<Widget> returnedWidget = service.findById(1l);

        // Assert the response
        Assertions.assertTrue(returnedWidget.isPresent(), "Widget was found");
        Assertions.assertSame(returnedWidget.get(), widget, "The widget returned was the same as the mock");
    }

    @Test
    @DisplayName("Test findById Not FOund")
    void testFindByIdNotFound() {
        // Setup our mock repository
        doReturn(Optional.empty()).when(repository).findById(3l);
        // Execute the service call
        Optional<Widget> widgets = service.findById(3l);
        // Assert the response (widget was not found)
        Assertions.assertFalse(widgets.isPresent(), "Widget was not found");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        // Setup our mock repository
        Widget widget1 = new Widget(1l, "Widget 1", "Description 1", 1);
        Widget widget2 = new Widget(2l, "Widget 2", "Description 2", 1);
        doReturn(Arrays.asList(widget1,widget2)).when(repository).findAll();

        // Execute the service call
        List<Widget> widgetList = service.findAll();
        // Assert the response (findAll returns 2 widgets)
        Assertions.assertEquals(2, widgetList.size(),"findAll returns 2 widgets");
    }

    @Test
    @DisplayName("Test save widget")
    void testSave() {
        // Setup our mock repository
        Widget widget3 = new Widget(3l,"Widget 3","Description 3", 1);
        doReturn(widget3).when(repository).save(widget3);

        // Execute the service call
        Widget widget = service.save(widget3);
        // Assert the response (saved widget is not null and version is incremented)
        Assertions.assertEquals(2,widget.getVersion());
    }

    @Test
    @DisplayName("Test deleteById repository.deleteById called with correct Id ")
    void testDelete() {
        // Execute the deleteById
        service.deleteById(1l);

        // Assert the deleteById was called use verify (https://www.baeldung.com/mockito-verify)
        verify(repository, times(1)).deleteById(1l);

    }
}
