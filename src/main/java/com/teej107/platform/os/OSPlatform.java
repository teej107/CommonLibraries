package com.teej107.platform.os;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by teej107 on 4/15/17.
 */
public class OSPlatform
{
	public Path getAppDataDirectory()
	{
		return Paths.get(System.getProperty("user.dir"));
	}

	public String getTerminate()
	{
		return "Exit";
	}

	public String getTrashName()
	{
		return "Trash";
	}

	public String getLocalAddress()
	{
		try
		{
			return InetAddress.getLocalHost().getHostAddress();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			return "localhost";
		}
	}
}
