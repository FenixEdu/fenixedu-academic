update FILE set PERMITTED_GROUP=replace(PERMITTED_GROUP, '.ResearchFunctionType', '.FunctionType') where PERMITTED_GROUP like '%ResearchFunctionType%';

