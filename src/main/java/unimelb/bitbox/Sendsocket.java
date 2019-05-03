package unimelb.bitbox;

import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Sendsocket {
    public static Document sendsocket(FileSystemManager.FileSystemEvent fileSystemEvent){

                FileSystemManager.FileDescriptor fd = fileSystemEvent.fileDescriptor;

                Document doc = new Document();
                Document do1 = new Document();
                if (fd != null) {
                    do1 = fd.toDoc();
                doc.append("fileDescriptor",do1);
                }
                doc.append("pathName",fileSystemEvent.pathName);
                doc.append("path",fileSystemEvent.path);
                doc.append("name",fileSystemEvent.name);
                doc.append("command",fileSystemEvent.event.toString()+"_REQUEST");
                Eventlist.addDocument(doc);
                return doc;


    }
    public static void sendtosocket(Document doc,Socket socket){

        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            out.write(doc.toJson()+"\n");
            System.out.println("our peer send " + doc.toJson() + " to " + socket.getInetAddress());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void sendDoc(Document doc){
        for (Socket socket : Connectionlist.returnsocketlist()
        ) {
                sendtosocket(doc,socket);
        }
    }
}
