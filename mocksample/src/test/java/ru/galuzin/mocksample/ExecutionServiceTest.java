package ru.galuzin.mocksample;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith( MockitoJUnitRunner.StrictStubs.class )
public class ExecutionServiceTest {

    @Mock
    SettingsHelper settingsHelper;

    @InjectMocks
    ExecutionService executionService;

//    @Before
//    public void before(){
//        MockitoAnnotations.initMocks(this);
//    }

    @Test
    public void execute() {
        Mockito.when(settingsHelper.getSomeSetting()).thenReturn("mocked");
        executionService.execute();
    }
}
