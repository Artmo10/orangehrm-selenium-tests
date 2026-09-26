package pim;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddEmployeePage;
import pages.DashboardPage;
import pages.EmployeeListPage;
import pages.LoginPage;
import pages.SidebarMenu;
import utils.Employee;
import utils.EmployeeDataProvider;
import utils.UniqueNameGenerator;

public class EmployeeLifecycleTests extends BaseTest {

    @Test(dataProvider = "employeeData", dataProviderClass = EmployeeDataProvider.class)
    public void testCreateAndSearchEmployee(Employee employee){

        // 1. Login
        LoginPage loginPage = new LoginPage(webDriver);
        DashboardPage dashboardPage = loginPage.loginAs("Admin", "admin123");
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "El login no llevó al Dashboard");

        // 2. Ir a PIM
        SidebarMenu sidebarMenu = new SidebarMenu(webDriver);
        EmployeeListPage employeeListPage = sidebarMenu.goToPIM();

        // 3. Ir a Add Employee
        AddEmployeePage addEmployeePage = employeeListPage.clickAddButton();

        // 4. Generar valores únicos para esta corrida
        String uniqueFirstName = UniqueNameGenerator.appendUniqueSuffix(employee.getFirstName());
        String uniqueUsername = UniqueNameGenerator.appendUniqueSuffix(employee.getUsername());

        // 5. Llenar datos del empleado
        addEmployeePage.typeFirstName(uniqueFirstName);
        addEmployeePage.typeMiddleName(employee.getMiddleName());
        addEmployeePage.typeLastName(employee.getLastName());
        String generatedEmployeeId = addEmployeePage.getGeneratedEmployeeId();
        Assert.assertFalse(generatedEmployeeId.isEmpty(), "No se pudo capturar el Employee Id generado");

        // 6. Activar y llenar datos de usuario
        addEmployeePage.enableCreateLoginDetails();
        addEmployeePage.typeUsername(uniqueUsername);
        addEmployeePage.typePassword(employee.getPassword());
        addEmployeePage.typeConfirmPassword(employee.getPassword());
        addEmployeePage.selectStatus(employee.getStatus());

        // 7. Guardar y verificar que se creó
        addEmployeePage.saveEmployee();
        Assert.assertTrue(
                addEmployeePage.isEmployeeSaved(),
                "El empleado '" + uniqueFirstName + "' no se guardó correctamente"
        );

        // 8. Volver al listado y buscar por Employee ID
        employeeListPage = addEmployeePage.goToEmployeeListTab();
        employeeListPage.typeEmployeeId(generatedEmployeeId);
        employeeListPage.clickSearch();

        // 9. Verificar que el empleado aparece en la grilla con su ID y su nombre
        String nameInGrid = employeeListPage.getNameForEmployeeId(generatedEmployeeId);

        Assert.assertNotNull(
                nameInGrid,
                "No apareció ninguna fila con el Employee Id '" + generatedEmployeeId + "'"
        );
        Assert.assertTrue(
                nameInGrid.startsWith(uniqueFirstName),
                "La fila con ID '" + generatedEmployeeId + "' muestra el nombre '" + nameInGrid
                        + "', se esperaba que empiece con '" + uniqueFirstName + "'"
        );
    }
}