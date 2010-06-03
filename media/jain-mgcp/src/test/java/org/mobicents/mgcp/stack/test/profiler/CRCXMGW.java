package org.mobicents.mgcp.stack.test.profiler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TooManyListenersException;

import jain.protocol.ip.mgcp.CreateProviderException;
import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.JainMgcpListener;
import jain.protocol.ip.mgcp.JainMgcpResponseEvent;
import jain.protocol.ip.mgcp.message.Constants;
import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import org.apache.log4j.Logger;
import org.mobicents.mgcp.stack.JainMgcpExtendedListener;
import org.mobicents.mgcp.stack.JainMgcpStackImpl;
import org.mobicents.mgcp.stack.JainMgcpStackProviderImpl;

public class CRCXMGW implements JainMgcpExtendedListener {

	private static Logger logger = Logger.getLogger(CRCXMGW.class);

	private boolean sendFailedResponse = false;

	JainMgcpStackProviderImpl mgwProvider;

	protected static final String CLIENT_ADDRESS = "127.0.0.1";

	protected static final String SERVER_ADDRESS = "127.0.0.1";

	protected static final int CA_PORT = 2724;

	protected static final int MGW_PORT = 2729;

	protected InetAddress mgwIPAddress = null;

	protected JainMgcpStackImpl mgwStack = null;

	long start;

	public CRCXMGW() {

	}

	public void createMgcpStack(JainMgcpListener listener) throws UnknownHostException, CreateProviderException,
			TooManyListenersException {
		mgwIPAddress = InetAddress.getByName(SERVER_ADDRESS);
		mgwStack = new JainMgcpStackImpl(mgwIPAddress, MGW_PORT);
		mgwProvider = (JainMgcpStackProviderImpl) mgwStack.createProvider();
		mgwProvider.addJainMgcpListener(listener);
	}

	public void transactionEnded(int handle) {

	}

	public void transactionRxTimedOut(JainMgcpCommandEvent command) {

	}

	public void transactionTxTimedOut(JainMgcpCommandEvent command) {
		logger.info("transactionTxTimedOut " + command);

	}

	public void processMgcpCommandEvent(JainMgcpCommandEvent jainmgcpcommandevent) {

		switch (jainmgcpcommandevent.getObjectIdentifier()) {
		case Constants.CMD_CREATE_CONNECTION:

			CreateConnectionResponse response = null;

			if (this.sendFailedResponse) {
				response = new CreateConnectionResponse(jainmgcpcommandevent.getSource(), ReturnCode.Endpoint_Unknown,
						new ConnectionIdentifier("0"));
				response.setTransactionHandle(jainmgcpcommandevent.getTransactionHandle());
			} else {
				String identifier = ((CallIdentifier) mgwProvider.getUniqueCallIdentifier()).toString();
				ConnectionIdentifier connectionIdentifier = new ConnectionIdentifier(identifier);

				response = new CreateConnectionResponse(jainmgcpcommandevent.getSource(),
						ReturnCode.Transaction_Executed_Normally, connectionIdentifier);

				response.setTransactionHandle(jainmgcpcommandevent.getTransactionHandle());
				try {
					// FIXME: we asume there is wildcard - "any of"
					CreateConnection cc = (CreateConnection) jainmgcpcommandevent;
					EndpointIdentifier wildcard = cc.getEndpointIdentifier();
					EndpointIdentifier specific = new EndpointIdentifier(wildcard.getLocalEndpointName().replace("$",
							"")
							+ "test-1", wildcard.getDomainName());
					response.setSpecificEndpointIdentifier(specific);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			mgwProvider.sendMgcpEvents(new JainMgcpEvent[] { response });

			break;
		default:
			logger.warn("This REQUEST is unexpected " + jainmgcpcommandevent);
			break;

		}

	}

	public void processMgcpResponseEvent(JainMgcpResponseEvent jainmgcpresponseevent) {

	}

	public static void main(String args[]) {
		final CRCXMGW mgw = new CRCXMGW();
		try {
			mgw.createMgcpStack(mgw);
			mgw.start = System.currentTimeMillis();
			System.out.println("Started CRCXMGW");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (CreateProviderException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}

	}

}
