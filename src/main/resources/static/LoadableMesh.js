class LoadableMesh extends BABYLON.Mesh {
    static alreadyRequested = {};
    static alreadyLoaded = {};

    constructor(instanceName, meshName) {
        super(instanceName);

        if (LoadableMesh.alreadyLoaded.hasOwnProperty(meshName)) {
            LoadableMesh.alreadyLoaded[meshName].applyToMesh(this);
        } else {
            if (LoadableMesh.alreadyRequested.hasOwnProperty(meshName)) {
                LoadableMesh.alreadyRequested[meshName].push(this);
            } else {
                LoadableMesh.alreadyRequested[meshName] = [this];
                fetch(`/mesh/${meshName}`)
                    .then(response => response.json())
                    .then(meshData => {
                        const normals = [];
                        BABYLON.VertexData.ComputeNormals(meshData.positions, meshData.indices, normals);
                        const vertexData = new BABYLON.VertexData();
                        vertexData.positions = meshData.positions;
                        vertexData.indices = meshData.indices;
                        vertexData.normals = normals;
                        LoadableMesh.alreadyLoaded[meshName] = vertexData;
                        for (const instance of LoadableMesh.alreadyRequested[meshName]) {
                            vertexData.applyToMesh(instance);
                        }
                    });
            }
        }
    }
}