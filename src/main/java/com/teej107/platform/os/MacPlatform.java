package com.teej107.platform.os;

import com.teej107.platform.Platform;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by teej107 on 4/15/17.
 */
public class MacPlatform extends OSPlatform
{
	@Override
	public Path getAppDataDirectory()
	{
		return Paths.get(Platform.getHome(), "Library", "Application Support");
	}

	@Override
	public String getTerminate()
	{
		return "Quit";
	}
}
