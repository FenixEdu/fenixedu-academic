#Correcting some fields that have chaveProjecto ou chaveCentrocusto = 0, updating to null

update mwgrant_entidadepagadora set chaveProjecto=null where chaveProjecto=0;

update mwgrant_entidadepagadora set chaveCentrocusto=null where chaveCentrocusto=0;
