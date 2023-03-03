package org.railway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {

    @Test(expected = Exception.class)
    public void test_main_with_one_param() throws Exception {
        String[] args = new String[0];
        Application.main(args);
    }

}
