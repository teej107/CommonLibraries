package com.teej107.platform.os;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by teej107 on 4/15/17.
 */
public class WindowsPlatform extends OSPlatform
{
	@Override
	public Path getAppDataDirectory()
	{
		return Paths.get(System.getenv("appdata"));
	}

	@Override
	public String getTrashName()
	{
		return "Recycle Bin";
	}
}
