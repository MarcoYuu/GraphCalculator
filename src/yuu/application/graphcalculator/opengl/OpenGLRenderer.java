package yuu.application.graphcalculator.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

	private int mWidth =0;
	private int mHeight =0;
	private float mAspectRatio =0.0f;
	
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglconfig) {
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
        mWidth =w;
        mHeight =h;
        mAspectRatio =mHeight/mWidth;
    }

    @Override
    public void onDrawFrame(GL10 gl) { 
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(-10.0f, 10.0f, -10*mAspectRatio, 10*mAspectRatio, 0.5f, -0.5f);
        
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        float[] vertices ={
        		-5.0f, -5.0f,
        		 5.0f, -5.0f,
        		-5.0f,  5.0f,
        		 5.0f,  5.0f,
        };
        FloatBuffer formed_buf =makeNativeFloatBuffer(vertices);
    
        gl.glEnable(GL10.GL_LINE_SMOOTH);
        gl.glColor4f(1.0f, 0.0f, 0.0f, 0.0f);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, formed_buf);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 4);      	
    }
    
    public static FloatBuffer makeNativeFloatBuffer(float[] buffer){
    	ByteBuffer byte_buf =ByteBuffer.allocateDirect(buffer.length*4);
    	byte_buf.order(ByteOrder.nativeOrder());
    	FloatBuffer float_buf =byte_buf.asFloatBuffer();
    	float_buf.put(buffer);
    	float_buf.position(0);
		return float_buf;
    }
}