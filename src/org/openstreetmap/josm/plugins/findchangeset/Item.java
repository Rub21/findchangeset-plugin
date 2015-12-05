/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openstreetmap.josm.plugins.findchangeset;

/**
 *
 * @author ruben
 */
  class Item
    {
        private int idchangeset;
        private String description;
 
        public Item(int id, String description)
        {
            this.idchangeset = id;
            this.description = description;
        }
 
        public int getId()
        {
            return idchangeset;
        }
 
        public String getDescription()
        {
            return description;
        }
 
        public String toString()
        {
            return description;
        }
    }