package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.io.InputStream;

public class EmployeeDataProvider {

    @DataProvider(name = "employeeData")
    public static Object[][] getEmployees() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = EmployeeDataProvider.class
                .getClassLoader()
                .getResourceAsStream("testdata/employees.json");

        if (inputStream == null) {
            throw new RuntimeException("No se encontró el archivo testdata/employees.json en el classpath");
        }

        Employee[] employees = mapper.readValue(inputStream, Employee[].class);

        Object[][] data = new Object[employees.length][1];
        for (int i = 0; i < employees.length; i++) {
            data[i][0] = employees[i];
        }
        return data;
    }
}