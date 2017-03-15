
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbDFDL;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;
import com.ibm.broker.plugin.MbXMLNS;
import com.ibm.broker.plugin.MbXMLNSC;


public class JCNMSGFLOW_JavaCompute extends MbJavaComputeNode {
	
	  String empno,ename;

    public void evaluate(MbMessageAssembly inAssembly) throws MbException {
        MbOutputTerminal out = getOutputTerminal("out");
        MbOutputTerminal alt = getOutputTerminal("alternate");
       
      

        MbMessage inMessage = inAssembly.getMessage();
        MbMessageAssembly outAssembly = null;
        try {
            // create new message as a copy of the input
            MbMessage outMessage = new MbMessage();
            outAssembly = new MbMessageAssembly(inAssembly, outMessage);
            // ----------------------------------------------------------
            // Add user code below
           
          
            String id1=inMessage.getRootElement().getLastChild().getLastChild().getLastChild().getValueAsString();
            
            Connection connection = this.getJDBCType4Connection("JCNDB", JDBC_TransactionType.MB_TRANSACTION_AUTO);
            String str="select * from MYTEMP where ID=?";
            PreparedStatement pstmt= connection.prepareStatement(str);
            pstmt.setString(1, id1);
            ResultSet rs=pstmt.executeQuery();
            while(rs.next())
            {
            empno = rs.getString("ID");
            ename=rs.getString("NAME");
           }
             
            
            // Statement stmt=connection.createStatement();
          
            
           
            outMessage.getRootElement().createElementAsFirstChild("Properties");
            outMessage.getRootElement().createElementAsLastChild("MQMD");
            MbElement XMLROOT=outMessage.getRootElement().createElementAsLastChild(MbXMLNSC.PARSER_NAME);           
            MbElement MsgRes1=XMLROOT.createElementAsLastChild(MbElement.TYPE_NAME,"ResponseMsg",null);
            MsgRes1.setNamespace("http://tempuri.org/JCNWSSET");   
             
                  
                    MsgRes1.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"ID","1");
                    MsgRes1.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"NAME","FirstRecord");
        
           
            // End of user code
            // ----------------------------------------------------------
        } catch (MbException e) {
            // Re-throw to allow Broker handling of MbException
            throw e;
        } catch (RuntimeException e) {
            // Re-throw to allow Broker handling of RuntimeException
            throw e;
        } catch (Exception e) {
            // Consider replacing Exception with type(s) thrown by user code
            // Example handling ensures all exceptions are re-thrown to be handled in the flow
            throw new MbUserException(this, "evaluate()", "", "", e.toString(),
                    null);
        }
        // The following should only be changed
        // if not propagating message to the 'out' terminal
        out.propagate(outAssembly);

    }

}

