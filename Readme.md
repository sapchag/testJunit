Установика Selenium Grid:

1)Скопировать папку WebDriver на диск C:
2)Запустить selenium_server.bat, selenium_hub.bat  

В файле PhpTravelConfig.xml при необходимости поменять адрес хаба в узле url     
<webdriver>
  <url>http://localhost:4444/wd/hub</url>
</webdriver> 

почитать можно здесь https://prostoitblog.ru/nastrojka-selenium-grid/
 
Для исключения браузера(ов) из теста, в файле PhpTravelConfig.xml,
в ветке <browsers> достаточно сделать узел соответствующего браузера пустым.
    <browsers>
        <CHROME>delete this tekst for disable browser</CHROME>
        <FIREFOX>delete this tekst for disable browser</FIREFOX>
        <IE></IE> 
        <OPERA>delete this tekst for disable browser</OPERA>
        <EDGE></EDGE>
    </browsers>
    
запуск проекта
mvn test
