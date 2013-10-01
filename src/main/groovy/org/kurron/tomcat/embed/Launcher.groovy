/*
 * Copyright year Ronald D. Kurr kurr@kurron.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */



package org.kurron.tomcat.embed

import org.apache.catalina.startup.Tomcat
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Option
import org.apache.commons.cli.OptionBuilder
import org.apache.commons.cli.Options

/**
 * Configures and launches the embedded Tomcat 7 container.
 */
class Launcher {
    public static void main(String[] args) {

        Options options = new Options()
        options.addOption( 'h', 'help', false, 'print this message' )
        options.addOption( 'p', 'port', true, 'port the application should bind to' )
        Option property  = OptionBuilder.withArgName( 'property=value' )
                                        .hasArgs(2)
                                        .withValueSeparator()
                                        .withDescription( 'use value for given property' )
                                        .create( 'D' )
        options.addOption( property )

        CommandLineParser parser = new GnuParser()
        CommandLine line = parser.parse( options, args )

        Tomcat tomcat = new Tomcat()

        if( line.hasOption( 'help' ) ) {
            HelpFormatter formatter = new HelpFormatter()
            formatter.printHelp( 'tomcat-7-embedded-experiment', options, true )
            System.exit( 0 )
        }

        if( line.hasOption( 'port' ) ) {
            tomcat.port = Integer.parseInt( line.getOptionValue( 'port' ) )
        }

        // this has to be set in the DEFAULT_JVM_OPTS variable in the start script
        println( "foo=${System.getProperty( 'foo', 'default value')}" )

        String location = 'src/main/webapp/'
        String path = new File(location).getAbsolutePath()
        println "configuring app with basedir: ${path}"
        tomcat.addWebapp('/', path)
        tomcat.start()
        tomcat.getServer().await()
    }
}
