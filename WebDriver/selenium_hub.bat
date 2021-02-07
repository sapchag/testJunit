cd C:\WebDriver\bin\
java -jar selenium-server-standalone-3.141.59.jar -role webdriver -hub http://localhost:4444/grid/register/ -port 5556 -nodeConfig "C:\WebDriver\bin\nodeconfig.json"