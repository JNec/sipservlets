/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mobicents.protocols.link;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.Stream;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 *
 * @author kulikov
 */
public class SelectorImpl implements StreamSelector {

    private Selector selector;
    private ArrayList<SelectorKey> selection = new ArrayList();
    protected ArrayList<DataLink> links = new ArrayList();

    public SelectorImpl() throws IOException {
        selector = SelectorProvider.provider().openSelector();
    }

    public Collection<SelectorKey> selectNow(int operation, int timeout) throws IOException {
        //try to register which are not connected yet
        for (DataLink link : links) {
            if (link.getState() == LinkState.NULL) {
                register(link);
            }
        }
        //cleaning key collector
        selection.clear();

        //selecting datagram channels ready for reading
        selector.selectNow();
        Set<SelectionKey> keys = selector.selectedKeys();

        for (SelectionKey key : keys) {
            //obtain respective data link and it's selector key
            SelectorKeyImpl sk = (SelectorKeyImpl) key.attachment();
            DataLink link = (DataLink) sk.getStream();
            
            link.isReadbale = false;
            link.isWritable = false;
            
            sk.isReadable = false;
            sk.isWritable = false;
            
            if (key.isReadable()) {
                //ask link to process data and it will return true if it was live data
                boolean liveData = link.processRx();
            
                //mark selector key as readbale and add to selection
                if (liveData && operation == StreamSelector.OP_READ) {
                    sk.isReadable = true;
                    selection.add((SelectorKey) key.attachment());
                }
            }
            
            if (key.isWritable()) {
                link.processTX();
                
                if (link.getState() == LinkState.ACTIVE && operation == StreamSelector.OP_WRITE) {
                    sk.isWritable = link.isWriteable();
                    if (!selection.contains(sk)){
                        selection.add(sk);
                    }
                }
            }
        }

        return selection;
    }

    protected SelectorKey register(Stream stream) throws IOException {
        //stote link in the list of connected links.
        if (!links.contains(stream)) {
            links.add((DataLink) stream);
        }

        //register it and update selector key
        SelectionKey key = ((DataLink) stream).channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        SelectorKeyImpl sk = new SelectorKeyImpl(key, stream, this);
        key.attach(sk);
        return sk;
    }

    public void setOperation(int v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getOperations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isReadOperation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isWriteOperation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isClosed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Stream> getRegisteredStreams() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}