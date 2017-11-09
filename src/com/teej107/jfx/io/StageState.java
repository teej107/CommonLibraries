package com.teej107.jfx.io;

import javafx.stage.Stage;

import java.awt.*;
import java.util.prefs.Preferences;

/**
 * JavaFX equivalent of {@link com.teej107.swing.io.WindowState}
 */
public class StageState
{
	private static final Dimension MAX_BOUNDS = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();

	protected static final String STAGE_X = "stage x";
	protected static final String STAGE_Y = "stage y";
	protected static final String STAGE_WIDTH = "stage width";
	protected static final String STAGE_HEIGHT = "stage height";
	protected static final String STAGE_MAXIMIZE = "stage maximize";

	protected Preferences prefs;
	protected Double defX, defY, defWidth, defHeight;

	public StageState(Preferences prefs)
	{
		this.prefs = prefs;
	}

	/**
	 * Constructs a StageState object and creates a {@link Preferences} object
	 *
	 * @param userRoot  true if the Preference object will be created at the user root level {@link Preferences#userRoot()}.
	 *                  Otherwise the Preferences will be created at system root level {@link Preferences#systemRoot()}
	 * @param firstNode The root node the preferences will be created at
	 * @param nodes     Any further path names to this node
	 * @see Preferences#node(String)
	 */
	public StageState(boolean userRoot, String firstNode, String... nodes)
	{
		StringBuilder sb = new StringBuilder(firstNode);
		for (String s : nodes)
		{
			sb.append('/').append(s);
		}

		this.prefs = (userRoot ? Preferences.userRoot() : Preferences.systemRoot()).node(sb.toString());
	}

	/**
	 * Constructs a StageState object and creates a {@link Preferences} object
	 *
	 * @param userRoot true if the Preference object will be created at the user root level {@link Preferences#userRoot()}.
	 *                 Otherwise the Preferences will be created at system root level {@link Preferences#systemRoot()}
	 * @param clazz    Class that the node uses to create the Preferences
	 * @see Preferences#userNodeForPackage(Class)
	 * @see Preferences#systemNodeForPackage(Class)
	 */
	public StageState(boolean userRoot, Class<?> clazz)
	{
		this.prefs = userRoot ? Preferences.userNodeForPackage(clazz) : Preferences.systemNodeForPackage(clazz);
	}

	public Double getDefaultX()
	{
		return defX;
	}

	public StageState setDefaultX(Double defX)
	{
		this.defX = defX;
		return this;
	}

	public Double getDefaultY()
	{
		return defY;
	}

	public StageState setDefaultY(Double defY)
	{
		this.defY = defY;
		return this;
	}

	public Double getDefaultWidth()
	{
		return defWidth;
	}

	public StageState setDefaultWidth(Double defWidth)
	{
		this.defWidth = defWidth;
		return this;
	}

	public Double getDefaultHeight()
	{
		return defHeight;
	}

	public StageState setDefaultHeight(Double defHeight)
	{
		this.defHeight = defHeight;
		return this;
	}

	public double getWidth()
	{
		Double defWidth = getDefaultWidth();
		return prefs.getDouble(STAGE_WIDTH, defWidth == null ? 800 : defWidth);
	}

	public StageState setWidth(double width)
	{
		prefs.putDouble(STAGE_WIDTH, width);
		return this;
	}

	public double getHeight()
	{
		Double defHeight = getDefaultHeight();
		return prefs.getDouble(STAGE_HEIGHT, defHeight == null ? 600 : defHeight);
	}

	public StageState setHeight(double height)
	{
		prefs.putDouble(STAGE_HEIGHT, height);
		return this;
	}

	public double getX()
	{
		Double defX = getDefaultX();
		return prefs.getDouble(STAGE_X, defX == null ? (MAX_BOUNDS.getWidth() / 2) - (getWidth() / 2) : defX);
	}

	public StageState setX(double x)
	{
		prefs.putDouble(STAGE_X, x);
		return this;
	}

	public double getY()
	{
		Double defY = getDefaultY();
		return prefs.getDouble(STAGE_Y, defY == null ? (MAX_BOUNDS.getHeight() / 2) - (getHeight() / 2) : defY);
	}

	public StageState setY(double y)
	{
		prefs.putDouble(STAGE_Y, y);
		return this;
	}

	public boolean isMaximized()
	{
		return prefs.getBoolean(STAGE_MAXIMIZE, false);
	}

	public StageState setMaximized(boolean maximized)
	{
		prefs.putBoolean(STAGE_MAXIMIZE, maximized);
		return this;
	}

	public void save(Stage stage)
	{
		boolean maximized = stage.isMaximized();
		setMaximized(maximized);
		if (!maximized && !stage.isIconified())
		{
			setWidth(stage.getWidth()).setHeight(stage.getHeight());
			setX(stage.getX()).setY(stage.getY());
		}
	}

	public void apply(Stage stage)
	{
		stage.setWidth(getWidth());
		stage.setHeight(getHeight());
		boolean maximized = isMaximized();
		stage.setMaximized(maximized);
		if (!maximized)
		{
			stage.setX(getX());
			stage.setY(getY());
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		StageState that = (StageState) o;

		if (prefs != null ? !prefs.equals(that.prefs) : that.prefs != null)
			return false;
		if (defX != null ? !defX.equals(that.defX) : that.defX != null)
			return false;
		if (defY != null ? !defY.equals(that.defY) : that.defY != null)
			return false;
		if (defWidth != null ? !defWidth.equals(that.defWidth) : that.defWidth != null)
			return false;
		return defHeight != null ? defHeight.equals(that.defHeight) : that.defHeight == null;
	}

	@Override
	public int hashCode()
	{
		int result = prefs != null ? prefs.hashCode() : 0;
		result = 31 * result + (defX != null ? defX.hashCode() : 0);
		result = 31 * result + (defY != null ? defY.hashCode() : 0);
		result = 31 * result + (defWidth != null ? defWidth.hashCode() : 0);
		result = 31 * result + (defHeight != null ? defHeight.hashCode() : 0);
		return result;
	}
}
