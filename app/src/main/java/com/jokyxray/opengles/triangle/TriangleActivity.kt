package com.jokyxray.opengles.triangle

import android.opengl.GLES31
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jokyxray.opengles.databinding.ActivityTriangleBinding
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TriangleActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTriangleBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.glSurfaceView.run {
            setEGLContextClientVersion(3)
            setRenderer(TriangleRender())
            renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        }
    }

    class TriangleRender : GLSurfaceView.Renderer {
        private lateinit var vertexBuffer: FloatBuffer
        private var mPositionHandle: Int = 0
        private var mProgram: Int = 0
        private var mColorHandle = 0

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
            GLES31.glClearColor(0.5F, 0.5F, 0.5F, 1.0F)
            GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)
            val byteBuffer = ByteBuffer.allocateDirect(triangleCoords.size * 4)
            byteBuffer.order(ByteOrder.nativeOrder())
            vertexBuffer = byteBuffer.asFloatBuffer()
            vertexBuffer.put(triangleCoords)
            vertexBuffer.position(0)
            val vertexShader = loadShader(GLES31.GL_VERTEX_SHADER,vertexShaderCode)
            val fragmentShader = loadShader(GLES31.GL_FRAGMENT_SHADER, fragmentShaderCode)
            mProgram = GLES31.glCreateProgram()
            GLES31.glAttachShader(mProgram,vertexShader)
            GLES31.glAttachShader(mProgram,fragmentShader)
            GLES31.glLinkProgram(mProgram)
        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            GLES31.glViewport(0,0,width, height)
        }

        override fun onDrawFrame(gl: GL10) {
            GLES31.glUseProgram(mProgram);

            //????????????????????????vPosition????????????
            mPositionHandle = GLES31.glGetAttribLocation(mProgram, "vPosition");
            //??????????????????????????????
            GLES31.glEnableVertexAttribArray(mPositionHandle);
            //??????????????????????????????
            GLES31.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false,
                vertexStride, vertexBuffer);
            //????????????????????????vColor???????????????
            mColorHandle = GLES31.glGetUniformLocation(mProgram, "vColor");
            //??????????????????????????????
            GLES31.glUniform4fv(mColorHandle, 1, color, 0);
            //???????????????
            GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, vertexCount);
            //???????????????????????????
            GLES31.glDisableVertexAttribArray(mPositionHandle);
        }

    }

    companion object {
        private val vertexShaderCode = """
            attribute vec4 vPosition;
             void main() {
                 gl_Position = vPosition;
             }
        """.trimIndent()
        private val fragmentShaderCode = """
            precision mediump float;
             uniform vec4 vColor;
             void main() {
                 gl_FragColor = vColor;
             }
        """.trimIndent()
        private val triangleCoords = floatArrayOf(
            0.5f, 0.5f, 0.0f,  // top
            -0.5f, -0.5f, 0.0f,  // bottom left
            0.5f, -0.5f, 0.0f // bottom right
        )
        private val color = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f) //??????
        private const val COORDS_PER_VERTEX = 3

        //????????????
        private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX

        //????????????????????????
        private const val vertexStride = COORDS_PER_VERTEX * 4 // ????????????????????????

        private fun loadShader(type: Int, shaderCode: String): Int {
            val shader = GLES31.glCreateShader(type)
            GLES31.glShaderSource(shader, shaderCode)
            GLES31.glCompileShader(shader)
            return shader
        }
    }
}