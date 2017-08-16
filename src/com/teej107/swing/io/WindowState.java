package com.teej107.swing.io;

import com.sun.istack.internal.Nullable;

import java.awt.*;
import java.util.Objects;
import java.util.prefs.Preferences;

/**
 * A class to save/retrieve a {@link Window}'s dimension, size, and state. This uses the {@link Preferences} API to save the information.
 * You may also pass some implementation of the interface through the {@link #WindowState(Preferences)} constructor
 *
 * @see Window
 * @see Preferences
 */
public class WindowState
{
	private static final Dimension MAX_BOUNDS = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();

	protected static final String WINDOW_X = "window x";
	protected static final String WINDOW_Y = "window y";
	protected static final String WINDOW_WIDTH = "window width";
	protected static final String WINDOW_HEIGHT = "window height";
	protected static final String WINDOW_STATE = "window state";

	protected Preferences prefs;
	protected Dimension defaultDim;
	protected Point defaultPoint;

	/**
	 * Constructs a WindowState object with the {@link Preferences} passed
	 *
	 * @param prefs Preferences
	 */
	public WindowState(Preferences prefs)
	{
		this.prefs = prefs;
	}

	/**
	 * Constructs a WindowState object and creates a {@link Preferences} object
	 *
	 * @param userRoot  true if the Preference object will be created at the user root level {@link Preferences#userRoot()}.
	 *                  Otherwise the Preferences will be created at system root level {@link Preferences#systemRoot()}
	 * @param firstNode The root node the preferences will be created at
	 * @param nodes     Any further path names to this node
	 * @see Preferences#node(String)
	 */
	public WindowState(boolean userRoot, String firstNode, String... nodes)
	{
		StringBuilder sb = new StringBuilder(firstNode);
		for (String s : nodes)
		{
			sb.append('/').append(s);
		}

		this.prefs = (userRoot ? Preferences.userRoot() : Preferences.systemRoot()).node(sb.toString());
	}

	/**
	 * Constructs a WindowState object and creates a {@link Preferences} object
	 *
	 * @param userRoot true if the Preference object will be created at the user root level {@link Preferences#userRoot()}.
	 *                 Otherwise the Preferences will be created at system root level {@link Preferences#systemRoot()}
	 * @param clazz    Class that the node uses to create the Preferences
	 * @see Preferences#userNodeForPackage(Class)
	 * @see Preferences#systemNodeForPackage(Class)
	 */
	public WindowState(boolean userRoot, Class<?> clazz)
	{
		this.prefs = userRoot ? Preferences.userNodeForPackage(clazz) : Preferences.systemNodeForPackage(clazz);
	}

	/**
	 * Get the default size. It's used if no size is found in the Preferences
	 *
	 * @return Default {@link Dimension} or null if no default is specified
	 */
	public Dimension getDefaultSize()
	{
		return defaultDim;
	}

	/**
	 * Set the default size to use if no size is found in the Preferences
	 *
	 * @param defaultDim the {@link Dimension} to use as default or null to specify no default
	 */
	public void setDefaultSize(@Nullable Dimension defaultDim)
	{
		this.defaultDim = defaultDim;
	}

	/**
	 * Get the default location. It's used if no location is found in the Preferences
	 *
	 * @return Default {@link Point} or null if no default is specified
	 */
	public Point getDefaultLocation()
	{
		return defaultPoint;
	}

	/**
	 * Set the default location to use if no size is found in the Preferences
	 *
	 * @param defaultPoint the {@link Point} to use as default or null to specify no default
	 */
	public void setDefaultLocation(@Nullable Point defaultPoint)
	{
		this.defaultPoint = defaultPoint;
	}

	/**
	 * Get the Preferences
	 *
	 * @return preferences used for this WindowState
	 * @see Preferences
	 */
	public Preferences getPreferences()
	{
		return prefs;
	}

	/**
	 * Set the location
	 *
	 * @param point location to save
	 */
	public void setLocation(Point point)
	{
		prefs.putInt(WINDOW_X, point.x);
		prefs.putInt(WINDOW_Y, point.y);
	}

	/**
	 * Get the location
	 *
	 * @return location that is saved. If none is found it will return the default point. If the default point is null,
	 * it will return a point on the screen that will center the window when visible.
	 */
	public Point getLocation()
	{
		Point defPoint = getDefaultLocation();
		Dimension size = getSize();
		int x = prefs.getInt(WINDOW_X, defPoint == null ? (MAX_BOUNDS.width / 2) - (size.width / 2) : defPoint.x);
		int y = prefs.getInt(WINDOW_Y, defPoint == null ? (MAX_BOUNDS.height / 2) - (size.height / 2) : defPoint.y);
		return new Point(x, y);
	}

	/**
	 * Set the size
	 *
	 * @param dimension size to save
	 */
	public void setSize(Dimension dimension)
	{
		prefs.putInt(WINDOW_WIDTH, dimension.width);
		prefs.putInt(WINDOW_HEIGHT, dimension.height);
	}

	/**
	 * Get the size
	 *
	 * @return size that is saved. If none is found it will return the default dimension. If the default dimension is null, it will return
	 * a dimension that is half the size of the screen space.
	 */
	public Dimension getSize()
	{
		Dimension defDimension = getDefaultSize();
		int width = prefs.getInt(WINDOW_WIDTH, defDimension == null ? MAX_BOUNDS.width / 2 : defDimension.width);
		int height = prefs.getInt(WINDOW_HEIGHT, defDimension == null ? MAX_BOUNDS.height / 2 : defDimension.height);
		return new Dimension(width, height);
	}

	/**
	 * Set the state of the window {@link Frame#setExtendedState(int)}
	 *
	 * @param windowState window state mask
	 * @see Frame#setExtendedState(int)
	 */
	public void setState(int windowState)
	{
		prefs.putInt(WINDOW_STATE, windowState);
	}

	/**
	 * Get the state of the window
	 *
	 * @return window state mask
	 * @see Frame#getExtendedState()
	 */
	public int getState()
	{
		return prefs.getInt(WINDOW_STATE, Frame.NORMAL);
	}

	/**
	 * Utility method to save a Window's location, size, and state all in one method.
	 * This method is preferred to use if you need to save the location, size, and state.
	 *
	 * @param window window whose location, size, and state and save
	 */
	public void save(Window window)
	{
		if (window instanceof Frame)
		{
			int state = ((Frame) window).getExtendedState();
			setState(state);
			if (state == Frame.MAXIMIZED_BOTH)
				return;
		}
		setSize(window.getSize());
		setLocation(window.getLocation());
	}

	/**
	 * Utility method to apply a location, size, and state to a Window.
	 * This method is preferred if you need to set the window's location,size, and state
	 *
	 * @param window window to set the location, size, and state
	 */
	public void apply(Window window)
	{
		window.setSize(getSize());
		if (window instanceof Frame)
		{
			int state = getState();
			((Frame) window).setExtendedState(state);
			if (state == Frame.MAXIMIZED_BOTH)
				return;
		}
		window.setLocation(getLocation());
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		WindowState that = (WindowState) o;

		return prefs.equals(that.prefs) && Objects.equals(defaultDim, that.defaultDim) && Objects.equals(defaultPoint, that.defaultPoint);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(prefs, defaultDim, defaultPoint);
	}
}
