package Enumeration;


import BasicComponent.GeneUnit;
import BasicComponent.Sample;

import java.util.ArrayList;
import java.util.Stack;


//This class performs the enumeration process on genes, human and viral miRs


public class EnumarationProcess_Quasi
{

        ArrayList<Sample> _genes;
        ArrayList<GeneUnit> _geneUnits;

        Clusters_Holder _cluster_holder;
        ArrayList<Cluster> _clusters;

        Node _treeRoot;
        //BufferedWriter _out;

        public ArrayList<Quasi_biclique> _quasi_bicliques = new ArrayList<Quasi_biclique>();

    public void work(ArrayList<Sample> samples, ArrayList<GeneUnit> geneunits){

    }

    private void construct_tree(){

    }

    public Node nested_appendBrothers(Sample sample) throws Exception {
        return appendBrothers(sample);
    }
    private Node appendBrothers(Sample sample)throws Exception
    {
        return null;
    }

    public boolean nested_deletePath(Node leaf){
        return deletePath(leaf);
    }
    private boolean deletePath(Node leaf){
        return false;
    }

    public void nested_genrateCliques(ArrayList<Sample> x_new,
                                      ArrayList<GeneUnit> mutual_human_mirs_new){
        generateCliques(x_new,mutual_human_mirs_new);
    }
    private void generateCliques(ArrayList<Sample> x_new,
                                 ArrayList<GeneUnit> mutual_human_mirs_new) {

    }

    private void nested_addClique(Quasi_biclique clique) {
        addClique(clique);

    }
    private void addClique(Quasi_biclique clique) {

    }


    //TODO make Node private
    public class Node
    {
        ArrayList<Node> _children;
        Node _father;
        ArrayList<Node> _leavesInSubtree;
        Sample _gene;
        ArrayList<GeneUnit> _mutual_geneUnit;

        ArrayList<Sample> _samples; //will be relevant only for the leaf nodes
        //to avoid path to the root

        int minNumOf_human_miRNAs = 2;
        int minNumOf_viral_miRNAs = 1;

        Node clone;

        Node(Sample sample)
        {
            _gene = sample;
            _children = new ArrayList<Node>();
            _leavesInSubtree = new ArrayList<Node>();
            _mutual_geneUnit = new ArrayList<GeneUnit>();
            _father = null;
        }

        //returns a new instance of the Node and all the subtree
        public Node nested_clone() {
            return clone();
        }
        protected Node clone()
        {
            Node newN = new Node(_gene);
            if (_children.size()==0) //leaf
            {
                newN._mutual_geneUnit = new ArrayList<GeneUnit>();
                newN._samples = new ArrayList<Sample>();

                for (int i=0; i<this._mutual_geneUnit.size();i++)
                {
                    newN._mutual_geneUnit.add(this._mutual_geneUnit.get(i));
                }
                for(int i=0; i<this._samples.size();i++)
                {
                    newN._samples.add(this._samples.get(i));
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
            return _gene +" "+ _leavesInSubtree.size();
        }
        //
        public boolean isValid()
        {
            return (this._mutual_geneUnit.size()>=minNumOf_human_miRNAs);
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
                    ans.add(0,current._gene);
                current = current._father;
            }
            return ans;
        }
    }
}
