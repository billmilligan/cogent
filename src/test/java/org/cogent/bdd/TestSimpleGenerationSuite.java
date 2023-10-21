package org.cogent.bdd;

import org.junit.platform.suite.api.ConfigurationParameter ;
import org.junit.platform.suite.api.IncludeEngines ;
import org.junit.platform.suite.api.Suite ;

import io.cucumber.core.options.Constants ;

// Refer to https://github.com/paawak/spring-boot-demo/tree/master/cucumber/cucumber-with-junit5-spring/src/test/java/com/swayam/demo/springbootdemo/cucumber

@Suite
@IncludeEngines ( "cucumber" )
// TODO: Replace with CucumberOptions if it will take it
@ConfigurationParameter ( key = Constants.GLUE_PROPERTY_NAME, value = "" )
@ConfigurationParameter ( key = Constants.FEATURES_PROPERTY_NAME, value = "classpath:generator/generator.feature" )
public class TestSimpleGenerationSuite {
	
}
