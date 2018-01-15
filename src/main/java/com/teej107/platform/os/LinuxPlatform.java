package com.teej107.platform.os;

import com.teej107.platform.Platform;

import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;

/**
 * Created by teej107 on 4/15/17.
 */
public class LinuxPlatform extends OSPlatform
{
	@Override
	public Path getAppDataDirectory()
	{
		return Paths.get(Platform.getHome());
	}

	@Override
	public String getLocalAddress()
	{
		try
		{
			Enumeration en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements())
			{
				NetworkInterface i = (NetworkInterface) en.nextElement();
				for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements(); )
				{
					InetAddress addr = (InetAddress) en2.nextElement();
					if (!addr.isLoopbackAddress())
					{
						if (addr instanceof Inet4Address)
						{
							return addr.getHostAddress();
						}
					}
				}
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		return Platform.getDefault().getLocalAddress();
	}
}
