package unimelb.bitbox;

import unimelb.bitbox.util.Document;
import unimelb.bitbox.util.FileSystemManager;
import unimelb.bitbox.util.FileSystemObserver;
import unimelb.bitbox.util.JSONRETURN;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

public class Funtional {
	private static FileSystemManager.FileDescriptor fd;

	public static void funtional(Document doc) throws IOException, NoSuchAlgorithmException {

		FileSystemObserver ob = Peer.getServerMain();
		FileSystemManager fsm = new FileSystemManager("share", ob); // should be replaced when generating

		switch (doc.getString("command")) {
		case "FILE_CREATE_REQEST":
			Document fileDescriper = (Document) doc.get("fileDescriptor");
			if (fsm.isSafePathName(doc.getString("pathName"))) { // check if the pathname is safe
				if (!fsm.fileNameExists(doc.getString("pathName"))) { // when the file name doesn't exist
					fsm.createFileLoader(doc.getString("pathName"), fileDescriper.getString("md5"), // create file
																									// loader
							Long.parseLong(fileDescriper.getString("fileSize")),
							Long.parseLong(fileDescriper.getString("lastModified")));

					if (fsm.checkShortcut(doc.getString("pathName"))) {
						break; // stop when there is a shortcut
					} else { // when there is no shortcut
						JSONRETURN.FILE_BYTES_REQUEST(fileDescriper, doc.getString("pathName"));
					}
				}
			} else

				break;
		case "FILE_DELETE":
			fsm.deleteFile(doc.getString("pathName"), doc.getLong("lastModicied"), doc.getString("md5"));
			break;
		case "FILE_MODIFY":
			break;
		case "DIRECTORY_CREATE_REQUEST":
			if (fsm.isSafePathName(doc.getString("pathName"))) { // check if the pathname is safe
				if (!fsm.fileNameExists(doc.getString("pathName"))) { // when the directory name doesn't exist
					fsm.makeDirectory(doc.getString("pathName"));
				}
			}
			break;
		case "DIRECTORY_DELETE_REQUEST":
			if (fsm.isSafePathName(doc.getString("pathName"))) { // check if the pathname is safe
				if (fsm.fileNameExists(doc.getString("pathName"))) { // when the directory name exist
					fsm.deleteDirectory(doc.getString("pathName"));
				}
			}
			break;
		default:
			break;
		}

	}

	private static void createfile(FileSystemManager.FileSystemEvent event, FileSystemManager fsm, ByteBuffer bb,
			long position) throws IOException, NoSuchAlgorithmException {
		String path = event.path;
		fd = event.fileDescriptor;
		fsm.createFileLoader(event.pathName, fd.md5, fd.fileSize, fd.lastModified);
		fsm.writeFile(event.pathName, bb, position);

	}
}
