package cn.typedefine.jms;
/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 * <p>
 * 1. Redistributions of source code must retain copyright
 * statements and notices.  Redistributions must also contain a
 * copy of this document.
 * <p>
 * 2. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * <p>
 * 3. The name "Exolab" must not be used to endorse or promote
 * products derived from this Software without prior written
 * permission of Exoffice Technologies.  For written permission,
 * please contact jima@intalio.com.
 * <p>
 * 4. Products derived from this Software may not be called "Exolab"
 * nor may "Exolab" appear in their names without prior written
 * permission of Exoffice Technologies. Exolab is a registered
 * trademark of Exoffice Technologies.
 * <p>
 * 5. Due credit should be given to the Exolab Project
 * (http://www.exolab.org/).
 * <p>
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p>
 * Copyright 2004 (C) Exoffice Technologies Inc. All Rights Reserved.
 * <p>
 * $Id: com.schd.mq.Sender.java,v 1.3 2005/11/18 03:28:01 tanderson Exp $
 */

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.jms.JMSException;
import javax.jms.Destination;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;


/**
 * <code>MessageProducer</code> example.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.3 $ $Date: 2005/11/18 03:28:01 $
 */
public class Sender {

    /**
     * Main line.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {

        //提前加载配置文件中的属性
        loadSysProps();

        Context context = null;
        ConnectionFactory factory = null;
        Connection connection = null;
        String factoryName = "ConnectionFactory";
        String destName = null;
        Destination dest = null;
        int count = 1;
        Session session = null;
        MessageProducer sender = null;
        String text = "Message ";

        if (args.length < 1 || args.length > 2) {
            System.out.println("usage: com.schd.mq.Sender <destination> [count]");
            System.exit(1);
        }

        destName = args[0];
        if (args.length == 2) {
            count = Integer.parseInt(args[1]);
        }

        try {
            // create the JNDI initial context.
            context = new InitialContext();

            // look up the ConnectionFactory
            factory = (ConnectionFactory) context.lookup(factoryName);

            // look up the Destination
            dest = (Destination) context.lookup(destName);

            // create the connection
            connection = factory.createConnection();

            // create the session
            session = connection.createSession(
                    false, Session.AUTO_ACKNOWLEDGE);

            // create the sender
            sender = session.createProducer(dest);

            // start the connection, to enable message sends
            connection.start();

            for (int i = 0; i < count; ++i) {
                TextMessage message = session.createTextMessage();
                message.setText(text + (i + 1));
                sender.send(message);
                System.out.println("Sent: " + message.getText());
            }
        } catch (JMSException exception) {
            exception.printStackTrace();
        } catch (NamingException exception) {
            exception.printStackTrace();
        } finally {
            // close the context
            if (context != null) {
                try {
                    context.close();
                } catch (NamingException exception) {
                    exception.printStackTrace();
                }
            }

            // close the connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private static void loadSysProps() {
        Properties properties = new Properties();
        try {
            File f1 = new File("");
            System.out.println(f1.getAbsolutePath());
            File f = new File("jndi.properties");
            if (f.exists()) {
                System.out.println("property file exist...");
            } else {
                System.out.println("property file not exist...");
            }
            //properties.load(Sender.class.getResourceAsStream("jdni.properties"));
            properties.load(new FileInputStream(f));
            Set<String> keys = properties.stringPropertyNames();
            System.out.println(keys);

            keys.forEach(key -> {
                String value = properties.getProperty(key);
                System.out.println("Setting properties, key->" + key + " value->" + value);
                System.setProperty(key, value);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
