
package org.linagora.linshare.webservice.test.soap;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.0
 * 2012-11-20T17:29:02.843+01:00
 * Generated source version: 2.7.0
 * 
 */
public final class DocumentSoapService_DocumentSoapServicePort_Client {

    private static final QName SERVICE_NAME = new QName("http://org/linagora/linshare/webservice/", "DocumentSoapWebService");

    private DocumentSoapService_DocumentSoapServicePort_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = DocumentSoapWebService.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        DocumentSoapWebService ss = new DocumentSoapWebService(wsdlURL, SERVICE_NAME);
        DocumentSoapService port = ss.getDocumentSoapServicePort();  
        
        {
        System.out.println("Invoking addDocumentXop...");
        org.linagora.linshare.webservice.test.soap.DocumentAttachement _addDocumentXop_arg0 = null;
        try {
            org.linagora.linshare.webservice.test.soap.Document _addDocumentXop__return = port.addDocumentXop(_addDocumentXop_arg0);
            System.out.println("addDocumentXop.result=" + _addDocumentXop__return);

        } catch (BusinessException_Exception e) { 
            System.out.println("Expected exception: BusinessException has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking getAvailableSize...");
        try {
            org.linagora.linshare.webservice.test.soap.SimpleLongValue _getAvailableSize__return = port.getAvailableSize();
            System.out.println("getAvailableSize.result=" + _getAvailableSize__return);

        } catch (BusinessException_Exception e) { 
            System.out.println("Expected exception: BusinessException has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking getDocuments...");
        try {
            java.util.List<org.linagora.linshare.webservice.test.soap.Document> _getDocuments__return = port.getDocuments();
            System.out.println("getDocuments.result=" + _getDocuments__return);

        } catch (BusinessException_Exception e) { 
            System.out.println("Expected exception: BusinessException has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking getUserMaxFileSize...");
        try {
            org.linagora.linshare.webservice.test.soap.SimpleLongValue _getUserMaxFileSize__return = port.getUserMaxFileSize();
            System.out.println("getUserMaxFileSize.result=" + _getUserMaxFileSize__return);

        } catch (BusinessException_Exception e) { 
            System.out.println("Expected exception: BusinessException has occurred.");
            System.out.println(e.toString());
        }
            }

        System.exit(0);
    }

}