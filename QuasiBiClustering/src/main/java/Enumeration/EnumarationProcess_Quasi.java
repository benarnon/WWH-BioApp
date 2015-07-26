package Enumeration;


import FeatureRelatedComponent.FeatureArray;
import FeatureRelatedComponent.GeneUnit;
import BasicComponent.Sample;
import FeatureRelatedComponent.Member;
import FeatureRelatedComponent.feature;
import Mains.EnumParams;

import java.util.ArrayList;
import java.util.Stack;


//This class performs the enumeration process on genes, human and viral miRs


public class EnumarationProcess_Quasi
{

        ArrayList<Sample> _samples;
        FeatureArray _featureArray;

        Clusters_Holder _cluster_holder;
        ArrayList<Cluster> _clusters;

        Node _treeRoot;
        //BufferedWriter _out;

        public ArrayList<Quasi_biclique> _quasi_bicliques = new ArrayList<Quasi_biclique>();

    public void work(ArrayList<Sample> samples, FeatureArray featureArray) throws Exception {
       _samples = samples;
       _featureArray = featureArray;
        _cluster_holder = new Clusters_Holder();

        construct_tree();

    }

    private void construct_tree() throws Exception {
        _treeRoot = new Node(new Sample(""));

        Sample dummySample = new Sample("dummy");
        Node dummy = new Node(dummySample);
        dummy._mutual_feature_array = this._featureArray;
        dummy._leavesInSubtree.add(dummy);
        dummy._father = _treeRoot;

        dummy._samples = new ArrayList<Sample>();

        _treeRoot._children.add(dummy);
        _treeRoot._leavesInSubtree.add(dummy);

        Sample sample;
        Node node;
        for (int i = 0; i < _samples.size() ; i++) {
            sample = _samples.get(i);
            if(sample.isValid()){
                node = appendBrothers(sample);
                if (node._children.size()>0)
                {
                    _treeRoot._children.add(node);
                    node._father = _treeRoot;
                    _treeRoot._leavesInSubtree.addAll(node._leavesInSubtree);
                }
            }
        }




    }

    public Node nested_appendBrothers(Sample sample) throws Exception {
        return appendBrothers(sample);
    }

    private Node appendBrothers(Sample sample)throws Exception
    {
        Node brother, cloneBrother, leaf;
        boolean ans;
        Node node = new Node(sample);

        for (int i = 0; i < _treeRoot._children.size(); i++) {
            brother = _treeRoot._children.get(i);
            cloneBrother = brother.clone();
            ans = true;

            for (int j = 0; j < cloneBrother._leavesInSubtree.size(); j++) {
                leaf = cloneBrother._leavesInSubtree.get(j);
                leaf._samples.add(sample);
                leaf._mutual_feature_array = createMutualNew(leaf._samples,leaf._mutual_feature_array);

                if(leaf.isValid()){
                    if(leaf._samples.size() >= EnumParams.minNumOfSamples){
                        generateCliques(leaf._samples,leaf._mutual_feature_array);
                    }
                }

                else{
                    ans = ans & deletePath(leaf);
                    cloneBrother._leavesInSubtree.remove(leaf);
                    cloneBrother._father = node;
                }
            }
            if(ans){
                node._children.add(cloneBrother);
                node._leavesInSubtree.addAll(cloneBrother._leavesInSubtree);
                cloneBrother._father = node;
            }
        }
        return node;
    }

    private FeatureArray createMutualNew(ArrayList<Sample> samples, FeatureArray mutual_feature_array) throws Exception {
        FeatureArray featureArray = new FeatureArray();
        for (int l = 0; l < mutual_feature_array.getNumOfFeatures(); l++) {
            feature fe = mutual_feature_array.getFeature(l);
            featureArray.createNewFeature(fe.getFeatureName());
            for (int i = 0; i < fe.getNumOfMember(); i++) {
                boolean check = true;
                Member me = fe.getMember(i);
                for (int j = 0; j < samples.size(); j++) {
                    feature f = samples.get(j).getFeature(l);
                    if (!f.isContainByName(me))
                        check = false;
                }
                if (check)
                    featureArray.addFeatureMemeber(fe.getFeatureName(), fe.getMember(i));

            }
        }
        return featureArray;
    }

    public boolean nested_deletePath(Node leaf){
        return deletePath(leaf);
    }

    private boolean deletePath(Node leaf){
        return false;
    }

    public void nested_genrateCliques(ArrayList<Sample> x_new,
                                      FeatureArray mutual_feature_array){
        generateCliques(x_new,mutual_feature_array);
    }
    private void generateCliques(ArrayList<Sample> x_new,
                                 FeatureArray mutual_feature_array) {
        Quasi_biclique clique = new Quasi_biclique(x_new,mutual_feature_array);
        addClique(clique);


    }

    private void nested_addClique(Quasi_biclique clique) {
        addClique(clique);

    }
    private void addClique(Quasi_biclique clique) {
        //checks if there is a smaller biclique full contained in it, if so, removes it
        //or this one if full contained in other

        Quasi_biclique temp;
        for(int i=0; i<_quasi_bicliques.size();i++)
        {
            temp = _quasi_bicliques.get(i);
            boolean ans = true;
            for (int j = 0; j <_featureArray.getNumOfFeatures(); j++) {
                if(!twoListsAreEqual(temp._mutual_feature_array.getFeature(j),clique._mutual_feature_array.getFeature(j)))
                    ans = false;
                    break;
            }
            if(ans)
            {
                if(clique._samples.containsAll(temp._samples))
                {
                    _quasi_bicliques.remove(i);
                    i--;
                }
                else if(temp._samples.containsAll(clique._samples))
                    return;
            }
        }

        _quasi_bicliques.add(clique);

    }

    private boolean twoListsAreEqual(feature list1, feature list2) {
        if(list1.containsAllnames(list2) && list2.containsAllnames(list1))
        {
            //System.out.println(list1 + " " + list2);
            return true;
        }
        else return false;
    }


    //TODO make Node private
    public class Node
    {
        ArrayList<Node> _children;
        Node _father;
        ArrayList<Node> _leavesInSubtree;
        Sample _sample;
        FeatureArray _mutual_feature_array;

        ArrayList<Sample> _samples; //will be relevant only for the leaf nodes
        //to avoid path to the root


        Node clone;

        Node(Sample sample)
        {
            _sample = sample;
            _children = new ArrayList<Node>();
            _leavesInSubtree = new ArrayList<Node>();
            _mutual_feature_array = new FeatureArray();
            _father = null;
        }

        //returns a new instance of the Node and all the subtree
        public Node nested_clone() {
            return clone();
        }
        protected Node clone()
        {
            Node newN = new Node(_sample);
            boolean check = true;
            if (_children.size()==0) //leaf
            {
                newN._mutual_feature_array = new FeatureArray();
                newN._samples = new ArrayList<Sample>();
                for (int j = 0; j <this._mutual_feature_array.getNumOfFeatures();  j++) {
                    try {
                        newN._mutual_feature_array.createNewFeature(_featureArray.getFeature(j).getFeatureName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < this._mutual_feature_array.getFeature(j).getNumOfMember(); i++) {
                        newN._mutual_feature_array.getFeature(j).addMember(this._mutual_feature_array.getFeature(j).cloneMember(i));
                    }
                    for (int i = 0; i < this._samples.size() && check; i++) {
                        newN._samples.add(this._samples.get(i));
                    }
                    check = false;
                }
            }

            Node child;
            for(int i=0;i<_children.size();i++)
            {
                child = _children.get(i).clone();
                newN._children.add(child);
                child._father = newN;
            }
            newN._leavesInSubtree = newN.getLeaves();

            newN.clone = this;

            return newN;
        }

        public String toString()
        {
            return _sample +" "+ _leavesInSubtree.size();
        }
        //
        public boolean isValid()
        {
            for (int i = 0; i < this._mutual_feature_array.getNumOfFeatures(); i++) {
                if(this._mutual_feature_array.getFeature(i).getNumOfMember() < EnumParams.minNumPerFeature[i])
                    return false;
            }
            return true;
        }

        private ArrayList<Node> getLeaves()
        {
            ArrayList<Node> answer = new ArrayList<Node>();
            Stack<Node> stack = new Stack<Node>();
            if (this._children.size()==0)
            {
                answer.add(this);
                return answer;
            }

            stack.push(this);
            Node poped;
            while(!stack.isEmpty())
            {
                poped = stack.pop();
                for (int i=poped._children.size()-1; i>=0;i--)
                {
                    Node current = poped._children.get(i);
                    if (current._children.size() == 0)
                        answer.add(current);
                    else
                        stack.push(current);
                }
            }

            return answer;
        }

        //path of genes
        public ArrayList<Sample> pathToTheRoot()
        {
            ArrayList<Sample> ans = new ArrayList<Sample>();
            //ans.add(_gene);
            Node current = _father;
            while(current!=null)
            {
                //System.out.println(current);
                if (!current.equals(_treeRoot))
                    ans.add(0,current._sample);
                current = current._father;
            }
            return ans;
        }
    }
}
