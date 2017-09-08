package pl.karolcyprowski.currat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pl.karolcyprowski.currat.monitor.NBPMonitor;

@SpringBootApplication
public class CurratApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurratApplication.class, args);
		runFileMonitor();
	}
	
	private static void runFileMonitor() {
		NBPMonitor monitor = new NBPMonitor();
		Thread monitorThread = new Thread(monitor, "NBPMonitor");
		monitorThread.start();		
	}
	
	
	
	
}
