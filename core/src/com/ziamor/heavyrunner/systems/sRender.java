package com.ziamor.heavyrunner.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ziamor.heavyrunner.components.*;

import java.util.Comparator;

public class sRender extends SortedIteratingSystem {
    private Mesh mesh;
    @Wire
    SpriteBatch batch;

    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cTexture> textureComponentMapper;
    ComponentMapper<cTextureRegion> textureRegionComponentMapper;
    ComponentMapper<cStaticShader> staticShaderComponentMapper;
    ComponentMapper<cStartRewind> startRewindComponentMapper;

    ShaderProgram staticShader;

    public sRender() {
        super(Aspect.all(cPosition.class).one(cTextureRegion.class, cTexture.class));

        /*(String name = "static";
        String vertexShader = Gdx.files.internal("shaders\\" + name + "\\vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders\\" + name + "\\fragment.glsl").readString();
        staticShader = new ShaderProgram(vertexShader, fragmentShader);

        if (staticShader.getLog().length() != 0)
           Gdx.app.debug("Shader Resolver System", staticShader.getLog());

        mesh = new com.badlogic.gdx.graphics.Mesh(true, 6, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));*/
    }

    @Override
    protected void process(int entityId) {
        cPosition pos = positionComponentMapper.get(entityId);
        cTexture tex = textureComponentMapper.get(entityId);
        cTextureRegion textureRegion = textureRegionComponentMapper.get(entityId);
        cStaticShader cStaticShader = staticShaderComponentMapper.get(entityId);

        if (tex != null)
            batch.draw(tex.texture, pos.x, pos.y);
        else if (textureRegion != null)
            batch.draw(textureRegion.textureRegion, pos.x, pos.y);

        /*if (cStaticShader != null && textureRegion != null) {
            cStartRewind startRewind = startRewindComponentMapper.get(entityId);
            if (startRewind == null) {
                staticShaderComponentMapper.remove(entityId);
                return;
            }
            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
            mesh = getTextureRegionMesh(pos, textureRegion);
            staticShader.begin();
            //shader.pedantic = false;
            textureRegion.textureRegion.getTexture().bind();
            staticShader.setUniformMatrix("u_projTrans", batch.getProjectionMatrix());
            staticShader.setUniformi("u_texture", 0);

            mesh.render(staticShader, GL20.GL_TRIANGLES);
            staticShader.end();

            batch.begin();
        }*/
    }

    @Override
    public Comparator<Integer> getComparator() {
        return new Comparator<Integer>() {
            @Override
            public int compare(Integer e1, Integer e2) {
                cPosition e1P = positionComponentMapper.get(e1);
                cPosition e2P = positionComponentMapper.get(e2);
                if (e1P == null)
                    return -1;
                if (e2P == null)
                    return 1;
                return (int) Math.signum(e1P.z - e2P.z);
            }
        };
    }

    @Override
    protected void begin() {
        super.begin();
        batch.begin();
    }

    @Override
    protected void end() {
        super.end();
        batch.end();
    }

    private Mesh getTextureRegionMesh(cPosition renderPositionComponent, cTextureRegion textureRegionComponent) {
        float x = renderPositionComponent.x;
        float y = renderPositionComponent.y;
        float width = 32; // TODO width and height should be passed as a param
        float height = 64;
        float fx2 = x + width;
        float fy2 = y + height;
        float u = textureRegionComponent.textureRegion.getU();
        float v = textureRegionComponent.textureRegion.getV2();
        float u2 = textureRegionComponent.textureRegion.getU2();
        float v2 = textureRegionComponent.textureRegion.getV();

        float[] verts = new float[30];
        int i = 0;

        //Top Left Vertex Triangle 1
        verts[i++] = x;   //X
        verts[i++] = fy2; //Y
        verts[i++] = 0;    //Z
        verts[i++] = u;   //U
        verts[i++] = v2;   //V

        //Top Right Vertex Triangle 1
        verts[i++] = fx2;
        verts[i++] = fy2;
        verts[i++] = 0;
        verts[i++] = u2;
        verts[i++] = v2;

        //Bottom Left Vertex Triangle 1
        verts[i++] = x;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = u;
        verts[i++] = v;

        //Top Right Vertex Triangle 2
        verts[i++] = fx2;
        verts[i++] = fy2;
        verts[i++] = 0;
        verts[i++] = u2;
        verts[i++] = v2;

        //Bottom Right Vertex Triangle 2
        verts[i++] = fx2;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = u2;
        verts[i++] = v;

        //Bottom Left Vertex Triangle 2
        verts[i++] = x;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = u;
        verts[i] = v;

        mesh.setVertices(verts);
        return mesh;
    }
}
