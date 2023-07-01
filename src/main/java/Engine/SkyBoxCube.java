package Engine;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_RGBA;
import static org.lwjgl.opengl.GL15.glTexImage2D;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class SkyBoxCube extends ShaderProgram
{
    private float size = 500f;
    private float [] VERTICES =
            {
                    -size,  size, -size,
                    -size, -size, -size,
                    size, -size, -size,
                    size, -size, -size,
                    size,  size, -size,
                    -size,  size, -size,

                    -size, -size,  size,
                    -size, -size, -size,
                    -size,  size, -size,
                    -size,  size, -size,
                    -size,  size,  size,
                    -size, -size,  size,

                    size, -size, -size,
                    size, -size,  size,
                    size,  size,  size,
                    size,  size,  size,
                    size,  size, -size,
                    size, -size, -size,

                    -size, -size,  size,
                    -size,  size,  size,
                    size,  size,  size,
                    size,  size,  size,
                    size, -size,  size,
                    -size, -size,  size,

                    -size,  size, -size,
                    size,  size, -size,
                    size,  size,  size,
                    size,  size,  size,
                    -size,  size,  size,
                    -size,  size, -size,

                    -size, -size, -size,
                    -size, -size,  size,
                    size, -size, -size,
                    size, -size, -size,
                    -size, -size,  size,
                    size, -size,  size
            };

    private final static String[] TEXTURE_FILE_NAMES = {"resources/skybox/right.png", "resources/skybox/left.png", "resources/skybox/top.png",
            "resources/skybox/bottom.png", "resources/skybox/back.png", "resources/skybox/front.png"};

    int vao, vbo, textureId;
    UniformsMap uniformsMap;

    public SkyBoxCube(List<ShaderModuleData> shaderModuleDataList)
    {
        super(shaderModuleDataList);
        uniformsMap = new UniformsMap(getProgramId());
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("viewMatrix");
        uniformsMap.createUniform("cubeMap");

        textureId = loadCubeMap(TEXTURE_FILE_NAMES);
//        System.out.println(textureId);
        setupVAOVBO();
    }

    public void setupVAOVBO()
    {
        //setup vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        //setup vbo
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, VERTICES, GL_STATIC_DRAW);
    }

    //draw setup pake kamera
    public void drawSetup(Camera camera, Projection projection)
    {
        bind();
        Matrix4f viewMatrix = new Matrix4f(camera.getViewMatrix());
        viewMatrix.m30(0);
        viewMatrix.m31(0);
        viewMatrix.m32(0);

        //isi uniform dengan variabel dari objek
        uniformsMap.setUniform("projectionMatrix",projection.getProjMatrix());
        uniformsMap.setUniform("viewMatrix",viewMatrix);
        uniformsMap.setUniform("cubeMap", 0);


        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureId);
    }

    //draw pake kamera
    public void draw(Camera camera, Projection projection)
    {
        drawSetup(camera, projection);
        glDrawArrays(GL_TRIANGLES, 0, 36);
//        glDisableVertexAttribArray(0);
//        glBindVertexArray(0);
    }
    private static TextureData decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer textureData = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            textureData = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(textureData, width * 4, PNGDecoder.Format.RGBA);
            textureData.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            System.exit(-1);
        }
//        System.out.println(fileName + " w " + width + " h " + height);
        return new TextureData(textureData, width, height);
    }

    private static int loadCubeMap(String[] textureFileNames)
    {
        GL.createCapabilities();
        int textureID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);

        for(int i = 0; i < textureFileNames.length; i++)
        {
            TextureData data = decodeTextureFile(textureFileNames[i]);
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, GL_RGBA, data.getWidth(), data.getHeight(), 0, GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        return textureID;
    }
}

class TextureData
{
    private int width, height;
    private ByteBuffer data;

    public TextureData(ByteBuffer data, int width, int height) {
        this.width = width;
        this.height = height;
        this.data = data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getBuffer() {
        return data;
    }
}
