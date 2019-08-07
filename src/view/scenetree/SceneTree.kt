package view.scenetree

import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.TreeView
import javafx.scene.control.TreeItem



/**
 * SceneTree fill scene root and treeVew with nodes
 */

class SceneTree(tree:TreeView<*>) {
    private val treeViewRoot = TreeItem("Root")
    val sceneRoot = Group()
    init {
        treeViewRoot.isExpanded = true
        tree.root=treeViewRoot
    }

    fun addAlltoRoot(vararg sceneNodes: Node){
        //Add to Scene
        sceneRoot.children.addAll(sceneNodes)
        //Add to tree
        for (sceneNode in sceneNodes){
            addNodeToTree(sceneNode,treeViewRoot)
        }
    }
    private fun addNodeToTree(sceneNode:Node, treeParent:TreeItem<String>){
        val treeItem = TreeItem(getNodeName(sceneNode))
        treeParent.children.add(treeItem)

            if (sceneNode is Group){
                  for (subNode in sceneNode.children){
                      addNodeToTree(subNode,treeItem)
                }
        }
    }

//    fun add(node: Node) {
//        rootGroup.children.add(node)
//        val treeItem = TreeItem(getNodeName(node))
//        rootItem.children.add(treeItem)
//    }

    private fun getNodeName(node:Node):String{
//        val str=""+node.id.let { node.id +" - " }
        var str=""
        if(node.id!=null) str=node.id +" - "
        return str + node.typeSelector
    }
}


