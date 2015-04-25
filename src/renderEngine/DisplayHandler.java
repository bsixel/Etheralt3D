package renderEngine;

//Ben Sixel

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import engineTester.WorldControl;

public class DisplayHandler {
	
	//Numbers
	private static int renderWidth;
	private static int renderHeight;
	private static final int fpsCap = 120;
	private static long lastFrameTime;
	private static long startTime = getCurrentTime() / 1000;
	private static float delta;
	private static long timerStart = 100;
	private static long timerCurrent;
	
	private static float FPS;
	private static float lastFPS;
	
	//Booleans
	public static boolean resizable = true;
	
	//Strings
	private static final String title = "Etheralt 3D";
	
	//Objects
	
	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			renderWidth = Display.getDesktopDisplayMode().getWidth();
			renderHeight = Display.getDesktopDisplayMode().getHeight();
			Display.setDisplayMode(new DisplayMode(renderWidth, renderHeight));
			Display.create(new PixelFormat(8, 8, 8, 8), attribs);
			Display.setResizable(resizable);
			Display.setTitle("Timer: " + timerStart);
			System.out.println("System OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, renderWidth, renderHeight);
		lastFrameTime = getCurrentTime();
		
		lastFPS = getCurrentTime();
		
	}
	
	public static float getAspectRatio() {
		return (Display.getWidth() / Display.getHeight());
	}
	
	public static void updateFPSTitle() {
		
		if (getCurrentTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + FPS);
			FPS = 0;
			lastFPS += 1000;
		}
		
		FPS ++;
		
	}
	
	/*public static void updateTimerTitle(boolean timerStarted) {
		
		if (timerStarted) {
			timerCurrent = (timerStart + startTime - getCurrentTime() / 1000);
			Display.setTitle("FPS: " + );
		}
		
	}*/
	
	public static void updateDisplay(WorldControl gameControl) {
		
		if (Display.wasResized()) {
			renderWidth = Display.getDesktopDisplayMode().getWidth();
			renderHeight = Display.getDesktopDisplayMode().getHeight();
		}
		Display.sync(fpsCap);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f; 
		lastFrameTime = currentFrameTime;
		
		updateFPSTitle();
		
	}
	
	public static boolean setDisplayMode(int width, int height, boolean fullscreen)
	{
	    if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height) && (Display.isFullscreen() == fullscreen))
	        return true;

	    try
	    {
	        DisplayMode targetDisplayMode = null;

	        if (fullscreen)
	        {
	            DisplayMode[] modes = Display.getAvailableDisplayModes();
	            int freq = 0;

	            for (DisplayMode current: modes)
	            {
	                if ((current.getWidth() == width) && (current.getHeight() == height))
	                {
	                    if ((targetDisplayMode == null) || (current.getFrequency() >= freq))
	                    {
	                        if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel()))
	                        {
	                            targetDisplayMode = current;
	                            freq = targetDisplayMode.getFrequency();
	                        }
	                    }
	                    if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency()))
	                    {
	                        targetDisplayMode = current;
	                        break;
	                    }
	                }
	            }
	        }
	        else
	        {
	            targetDisplayMode = new DisplayMode(width, height);
	        }

	        if (targetDisplayMode == null)
	        {
	            System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
	            return false;
	        }
	        Display.setDisplayMode(targetDisplayMode);
	        Display.setFullscreen(fullscreen);

	        System.out.println("Selected DisplayMode: " + targetDisplayMode.toString());

	        return true;
	    }
	    catch (LWJGLException e)
	    {
	        System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
	    }

	    return false;
	}
	
	public static float getFrameTimeSeconds() {
		
		return delta;
		
	}

	public static float tick() {

		return delta;

	}
	
	public static void closeDisplay() {
		
		Display.destroy();
		
	}
	
	private static long getCurrentTime() {
		
		return Sys.getTime()*1000/Sys.getTimerResolution();
		
	}

	public static int getRenderWidth() {
		return renderWidth;
	}

	public static void setRenderWidth(int renderWidth) {
		DisplayHandler.renderWidth = renderWidth;
	}

	public static int getRenderHeight() {
		return renderHeight;
	}

	public static void setRenderHeight(int renderHeight) {
		DisplayHandler.renderHeight = renderHeight;
	}
	
	public static void updateStartTime() {
		startTime = getCurrentTime() / 1000;
	}
	
	public static void updateTimerStart() {
		if (timerCurrent < timerStart) {
			timerStart = timerCurrent;
		}
	}

}
