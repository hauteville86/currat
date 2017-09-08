package pl.karolcyprowski.currat.monitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import pl.karolcyprowski.currat.config.MvcConfiguration;
import pl.karolcyprowski.currat.core.TaskHandler;

public class NBPMonitor implements Runnable, DisposableBean{
	
	static Logger logger = Logger.getLogger(NBPMonitor.class);
	
	@Autowired
	private TaskHandler taskHandler;
	
	private Thread thread;
	
	private long refreshTime = 300000;
	
	private boolean isRefreshCondition;
	
	public NBPMonitor() {
		this.isRefreshCondition = true;
		this.thread = new Thread(this);
	}
	
	public NBPMonitor(TaskHandler taskHandler) {
		this.taskHandler = taskHandler;
		this.isRefreshCondition = true;
		this.thread = new Thread(this);
	}
	
	@Override
	public void run() {
		File templateFile = new File("dir.txt");
		URL url = generateURL();
		if (url != null) {
			
			try {
				while(true) {
					Thread.currentThread().sleep(refreshTime);
					URLConnection connection = url.openConnection();
					InputStream in = connection.getInputStream();
					File temporaryFile = new File("dir-tmp.txt");
					FileOutputStream fos = new FileOutputStream(temporaryFile);
					byte[] buf = new byte[512];
					while (true) {
					    int len = in.read(buf);
					    if (len == -1) {
					        break;
					    }
					    fos.write(buf, 0, len);
					}
					in.close();
					fos.flush();
					fos.close();
					byte[] f1 = Files.readAllBytes(templateFile.toPath());
					byte[] f2 = Files.readAllBytes(temporaryFile.toPath());
					if(!Arrays.equals(f1, f2)) {
						try {
							if(taskHandler == null) {
								taskHandler = MvcConfiguration.getContext().getBean(TaskHandler.class);
								if (taskHandler != null) {
									logger.info("TaskHandler correctly injected");
								}
							}
							taskHandler.loadRecentDataFromWeb();
							templateFile.delete();
							temporaryFile.renameTo(new File("dir.txt"));
							templateFile = temporaryFile;
							logger.warn("File has been changed...");
						} catch (NullPointerException e) {
							e.printStackTrace();
							logger.error("Task Handler hasn't been initiated for NBPMonitor");
						}												
						
					} else {
						logger.info("No updates available");
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void destroy() {
		this.isRefreshCondition = false;
	}
	
	private static URL generateURL() {
		try {
			return new URL("http://www.nbp.pl/kursy/xml/dir.txt");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	
}
