package edu.escuelaing.arep.webservice;

import edu.escuelaing.arep.annotations.Web;

public class WebServiceHello {
	
	@Web("hello")
	public static String Hello() {
		return (" <html lang=\"en\">\r\n" + 
				"\r\n" + 
				"<head>\r\n" + 
				"    <meta charset=\"UTF-8\">\r\n" + 
				"    <title>Hello</title>\r\n" + 
				"</head>\r\n" + 
				"\r\n" + 
				"<body>\r\n" + 
				"    <div id=\"content\" align=\"center\">\r\n" + 
				"        <h1>Hello</h1>\r\n" + 
				"    </div>\r\n" + 
				"</body>\r\n" + 
				"\r\n" + 
				"</html>"); 
	}

}
