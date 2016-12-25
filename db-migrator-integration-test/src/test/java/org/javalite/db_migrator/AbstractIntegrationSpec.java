/*
Copyright 2009-2016 Igor Polevoy

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License. 
*/

package org.javalite.db_migrator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.maven.cli.MavenCli;
import static org.javalite.db_migrator.DbUtils.closeQuietly;

public abstract class AbstractIntegrationSpec {

    protected String execute(String dir, String... args) throws IOException, InterruptedException {
        OutputStream outOs = null;
        PrintStream outPs = null;
        OutputStream errOs = null;
        PrintStream errPs = null;
        try {
            outOs = new ByteArrayOutputStream();
            outPs = new PrintStream(outOs);
            errOs = new ByteArrayOutputStream();
            errPs = new PrintStream(errOs);
            MavenCli cli = new MavenCli();
            int code = cli.doMain(args, dir, outPs, errPs);
            String out = outOs.toString();
            String err = errOs.toString();
            if (code != 0) {
                System.out.println("TEST MAVEN EXECUTION START >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.print("Executing: mvn");
                for (String arg : args) {
                    System.out.print(' ');
                    System.out.print(arg);
                }
                System.out.println();
                System.out.print("Exit code: " + code);
                System.out.print(out);
                System.err.print(err);
                System.out.println("TEST MAVEN EXECUTION END <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            }
            return out + err;
        } finally {
            closeQuietly(errPs);
            closeQuietly(errOs);
            closeQuietly(outPs);
            closeQuietly(outOs);
        }
    }
}
