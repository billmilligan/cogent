package org.cogent.model.output;

import lombok.Builder ;

@Builder
public record StyleConfig (
		String name, 
		IndentationConfig indentation, 
		BracePosition bracePosition, 
		ParenthesesPosition parenthesesPosition, 
		Whitespace whitespace, 
		BlankLines blankLines, 
		NewLines newLines, 
		LineWrap lineWrap,
		Comments comments
	) {

	public static enum IndentationType { Tabs, Spaces }
	public static enum TextBlockIndentation { DoNotTouch, IndentByOne, DefaultForWrappedLines, IndentOnColumn }
	public static enum BracePositionValue { SameLine, NextLine, NextLineIndented, NextLineOnWrap }
	public static enum ParenthesesPositionValue { SameLineAsContent, SeparateLinesIfWrapped, SeparateLinesIfNotEmpty, SeparateLines }
	public static enum BracedCodeOnOneLineValue { Never, IfEmpty, IfAtOnceOneItem, IfFitsInWidthLimit }
	public static enum LineWrappingPolicy { DoNotWrap, WrapWhereNecessary, WrapFirstElementOthersWhereNecessary, WrapAllElementsEachOnePerLine, WrapAllElementsIndentAllButFirst, WrapAllElementsIndentAllButFirstUnlessNecessary }
	public static enum IndentationPolicy { DefaultIndentation, IndentByOne, IndentOnColumn }
	public static enum WrapOperatorPolicy { WrapBeforeOperators, WrapAfterOperators }
	public static enum AlignJavadocTagsInColumns { AlignNamesAndDescription, AlignDescriptionsGroupedByType, AlignDescriptionsToTagWidth, DoNotAlign }

	@Builder
	public static record IndentationConfig (
		IndentationType type,
		boolean useSpacesToIndentWrappedLines,
		int indentationSpacesCount,
		int tabSize,
		TextBlockIndentation textBlockIndentation,
		IndentationElements indentationElements,
		AlignmentInColumns alignmentInColumns
	) { ; }

	@Builder
	public static record IndentationElements (
		boolean declarationWithinClassBody,
		boolean declarationWithinEnumDeclaration,
		boolean declarationWithinEnumConstants,
		boolean declarationWithinAnnotationDeclaration,
		boolean declarationWithinRecordDeclaration,
		boolean statementWithinConstructorBody,
		boolean statementWithinMethodBody,
		boolean statementWithinBlock,
		boolean statementWithinSwitchBody,
		boolean statementWithinCaseBody,
		boolean breakWithinSwitchBody,
		boolean emptyLines
	) { ; }

	@Builder
	public static record AlignmentInColumns (
		boolean fieldDeclarations, 
		boolean variableDeclarations, 
		boolean assignmentStatements, 
		boolean useSpacesForAlign, 
		int blankLinesSeparatingIndependentGroups
	) { ; }

	@Builder
	public static record BracePosition (
		BracePositionValue classOrInterfaceDeclaration, 
		BracePositionValue anonymousClassDeclaration, 
		BracePositionValue constructorDeclaration, 
		BracePositionValue methodDeclaration, 
		BracePositionValue enumDeclaration, 
		BracePositionValue enumConstantBody, 
		BracePositionValue recordDeclaration, 
		BracePositionValue recordConstructor, 
		BracePositionValue annotationTypeDeclaration, 
		BracePositionValue block, 
		BracePositionValue blockInCaseStatement, 
		BracePositionValue switchExpression, 
		BracePositionValue switchStatement, 
		BracePositionValue arrayExpression, 
		boolean keepEmptyArrayInitializerOnOneLine, 
		BracePositionValue lambdaBody
	) { ; }

	@Builder
	public static record ParenthesesPosition (
		ParenthesesPositionValue methodDeclaration, 
		ParenthesesPositionValue constructorDeclaration, 
		ParenthesesPositionValue methodInvocation, 
		ParenthesesPositionValue constructorInvocation, 
		ParenthesesPositionValue enumConstantDeclaration, 
		ParenthesesPositionValue recordDeclaration, 
		ParenthesesPositionValue annotation, 
		ParenthesesPositionValue lambdaDeclaration, 
		ParenthesesPositionValue ifStatement, 
		ParenthesesPositionValue whileStatement, 
		ParenthesesPositionValue doWhileStatement, 
		ParenthesesPositionValue forStatement, 
		ParenthesesPositionValue switchStatement, 
		ParenthesesPositionValue tryClause, 
		ParenthesesPositionValue catchClause
	) { ; }

	@Builder
	public static record Whitespace (
		WhitespaceDeclaration declaration,
		WhitespaceControlStatement controlStatement,
		WhitespaceExpression expression,
		WhitespaceArray array,
		WhitespaceParameterizedType parameterizedType
	) { ; }

	@Builder
	public static record WhitespaceDeclaration (
		ClassSpace classSpace,
		FieldSpace fieldSpace,
		LocalVariableSpace localVariableSpace,
		ConstructorSpace constructorSpace,
		MethodSpace methodSpace,
		LabelSpace labelSpace,
		AnnotationSpace annotationSpace,
		EnumSpace enumSpace,
		AnnotationTypeSpace annotationTypeSpace,
		RecordSpace recordSpace,
		LambdaSpace lambdaSpace
	) { ; }

	@Builder
	public static record ClassSpace (
		boolean beforeOpeningBraceNamedClass, 
		boolean beforeOpeningBraceAnonymousClass, 
		boolean beforeCommaImplementsClause, 
		boolean afterCommaImplementsClause, 
		boolean beforeCommaPermitsClause, 
		boolean afterCommaPermitsClause
	) { ; }

	@Builder
	public static record FieldSpace (
		boolean beforeCommaMultipleFields, 
		boolean afterCommaMultipleFields
	) { ; }

	@Builder
	public static record LocalVariableSpace (
		boolean beforeCommaMultipleFields, 
		boolean afterCommaMultipleFields
	) { ; }

	@Builder
	public static record ConstructorSpace (
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeClosingParenthesis, 
		boolean afterClosingParenthesis, 
		boolean betweenEmptyParenthesis, 
		boolean beforeOpeningBrace, 
		boolean beforeCommaInParameters, 
		boolean afterCommaInParameters, 
		boolean beforeEllipsesInVarargs, 
		boolean afterEllipsesInVarargs, 
		boolean beforeCommaInThrows,
		boolean afterCommaInThrows
	) { ; }

	@Builder
	public static record MethodSpace (
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeClosingParenthesis, 
		boolean betweenEmptyParenthesis, 
		boolean beforeOpeningBrace, 
		boolean beforeCommaInParameters, 
		boolean afterCommaInParameters, 
		boolean beforeEllipsesInVarargs, 
		boolean afterEllipsesInVarargs, 
		boolean beforeCommaInThrows,
		boolean afterCommaInThrows
	) { ; }

	@Builder
	public static record LabelSpace (
		boolean beforeColon, 
		boolean afterColon
	) { ; }

	@Builder
	public static record AnnotationSpace (
		boolean afterAt, 
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeComma, 
		boolean afterComma, 
		boolean beforeClosingParenthesis 
	) { ; }

	@Builder
	public static record EnumSpace (
		boolean beforeOpeningBraceInDeclaration, 
		boolean beforeCommaBetweenConstants, 
		boolean afterCommaBetweenConstants, 
		boolean beforeOpeningParenthesisInConstantArguments, 
		boolean afterOpeningParenthesisInConstantArguments, 
		boolean betweenEmptyParenthesisInConstantArguments,  
		boolean beforeCommaInConstantArguments,  
		boolean afterCommaInConstantArguments,  
		boolean beforeClosingParenthesisInConstantArguments,  
		boolean beforeOpeningBraceOfConstantBody
	) { ; }

	@Builder
	public static record AnnotationTypeSpace (
		boolean beforeAt, 
		boolean afterAt, 
		boolean beforeOpeningBrace, 
		boolean beforeOpeningParenthesisInAnnotationTypeMembers, 
		boolean betweenParenthesisInAnnotationTypeMembers
	) { ; }

	@Builder
	public static record RecordSpace (
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeCommaInRecordComponents, 
		boolean afterCommaInRecordComponents, 
		boolean beforeClosingParenthesis, 
		boolean beforeOpeningBraceInDeclaration, 
		boolean beforeOpeningBraceInRecordConstructor
	) { ; }

	@Builder
	public static record LambdaSpace (
		boolean beforeArrowOperator, 
		boolean afterArrowOperator
	) { ; }

	@Builder
	public static record WhitespaceControlStatement (
		boolean beforeSemicolon,
		WhitespaceBlockStatement blockStatement,
		WhitespaceIfElseStatement ifElseStatement,
		WhitespaceForStatement forStatement,
		WhitespaceSwitchStatement switchStatement,
		WhitespaceWhileStatement whileStatement,
		WhitespaceDoWhileStatement doWhileStatement,
		WhitespaceSynchronizedStatement synchronizedStatement,
		WhitespaceTryWithResourcesClause tryWithResourcesClause,
		WhitespaceCatchClause catchClause,
		WhitespaceAssertStatement assertStatement,
		WhitespaceReturnStatement returnStatement,
		WhitespaceThrowStatement throwStatement
	) { ; }

	@Builder
	public static record WhitespaceBlockStatement (
		boolean beforeOpeningBrace, 
		boolean afterClosingBrace
	) { ; }

	@Builder
	public static record WhitespaceIfElseStatement (
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeClosingParenthesis
	) { ; }

	@Builder
	public static record WhitespaceForStatement (
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeClosingParenthesis, 
		boolean beforeCommaInInitialization, 
		boolean afterCommaInInitialization, 
		boolean beforeCommaInIncrements, 
		boolean afterCommaInIncrements, 
		boolean beforeSemicolon, 
		boolean afterSemicolon, 
		boolean beforeColon, 
		boolean afterColon
	) { ; }

	@Builder
	public static record WhitespaceSwitchStatement (
		boolean beforeColonInCase,
		boolean beforeColonInDefault, 
		boolean beforeArrowInCase, 
		boolean afterArrowInCase, 
		boolean beforeArrowInDefault, 
		boolean afterArrowInDefault, 
		boolean beforeCommaInCaseExpressions, 
		boolean afterCommaInCaseExpressions, 
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeClosingParenthesis, 
		boolean beforeOpeningBrace
	) { ; }

	@Builder
	public static record WhitespaceWhileStatement (
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeClosingParenthesis
	) { ; }

	@Builder
	public static record WhitespaceDoWhileStatement (
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeClosingParenthesis
	) { ; }

	@Builder
	public static record WhitespaceSynchronizedStatement (
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeClosingParenthesis
	) { ; }

	@Builder
	public static record WhitespaceTryWithResourcesClause (
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeSemicolon, 
		boolean afterSemicolon, 
		boolean beforeClosingParenthesis
	) { ; }

	@Builder
	public static record WhitespaceCatchClause (
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeClosingParenthesis
	) { ; }

	@Builder
	public static record WhitespaceAssertStatement (
		boolean beforeColon, 
		boolean afterColon
	) { ; }

	@Builder
	public static record WhitespaceReturnStatement (
		boolean beforeParenthesizedExpressions
	) { ; }

	@Builder
	public static record WhitespaceThrowStatement (
		boolean beforeParenthesizedExpressions
	) { ; }

	@Builder
	public static record WhitespaceExpression (
		WhitespaceFunctionInvocation functionInvocation,
		WhitespaceUnaryOperator unaryOperator,
		WhitespaceBinaryOperator binaryOperator,
		WhitespaceConditionalOperator conditionalOperator,
		WhitespaceAssignmentOperator assignmentOperator,
		WhitespaceParenthesizedExpression parenthesizedExpression,
		WhitespaceTypeCast typeCast
	) { ; }

	@Builder
	public static record WhitespaceFunctionInvocation (
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeClosingParenthesis, 
		boolean betweenEmptyParentheses, 
		boolean beforeCommaInMethodArguments, 
		boolean afterCommaInMethodArguments, 
		boolean beforeCommaInObjectAllocationArguments, 
		boolean afterCommaInObjectAllocationArguments, 
		boolean beforeCommaInExplicitConstructorCall, 
		boolean afterCommaInExplicitConstructorCall
	) { ; }

	@Builder
	public static record WhitespaceUnaryOperator (
		boolean beforePostfixOperator, 
		boolean afterPostfixOperator, 
		boolean beforePrefixOperator, 
		boolean afterPrefixOperator, 
		boolean beforeUnaryOperator, 
		boolean afterUnaryOperator, 
		boolean afterLogicalNotOperator
	) { ; }

	@Builder
	public static record WhitespaceBinaryOperator (
		boolean beforeMultiplicativeOperator, 
		boolean afterMultiplicativeOperator, 
		boolean beforeAdditiveOperator, 
		boolean afterAdditiveOperator, 
		boolean beforeStringConcatenationOperator, 
		boolean afterStringConcatenationOperator, 
		boolean beforeShiftOperator, 
		boolean afterShiftOperator, 
		boolean beforeRelationalOperator, 
		boolean afterRelationalOperator, 
		boolean beforeBitwiseOperator, 
		boolean afterBitwiseOperator, 
		boolean beforeLogicalOperator, 
		boolean afterLogicalOperator
	) { ; }

	@Builder
	public static record WhitespaceConditionalOperator (
		boolean beforeQuestionMark, 
		boolean afterQuestionMark, 
		boolean beforeColon, 
		boolean afterColon
	) { ; }

	@Builder
	public static record WhitespaceAssignmentOperator (
		boolean beforeAssignmentOperator, 
		boolean afterAssignmentOperator
	) { ; }

	@Builder
	public static record WhitespaceParenthesizedExpression (
		boolean beforeOpeningParenthesis, 
		boolean afterOpeningParenthesis, 
		boolean beforeClosingParenthesis
	) { ; }

	@Builder
	public static record WhitespaceTypeCast (
		boolean afterOpeningParenthesis, 
		boolean beforeClosingParenthesis, 
		boolean afterClosingParenthesis
	) { ; }

	@Builder
	public static record WhitespaceArray (
		WhitespaceArrayDeclaration declaration, 
		WhitespaceArrayAllocation allocation,
		WhitespaceArrayInitializer initializer,
		WhitespaceArrayAccess access
	) { ; }

	@Builder
	public static record WhitespaceArrayDeclaration (
		boolean beforeOpeningBracket, 
		boolean betweenEmptyBrackets
	) { ; }

	@Builder
	public static record WhitespaceArrayAllocation (
		boolean beforeOpeningBracket, 
		boolean afterOpeningBracket, 
		boolean beforeClosingBracket, 
		boolean afterClosingBracket
	) { ; }

	@Builder
	public static record WhitespaceArrayInitializer (
		boolean beforeOpeningBrace, 
		boolean afterOpeningBrace, 
		boolean beforeClosingBrace, 
		boolean beforeComma, 
		boolean afterComma, 
		boolean betweenEmptyBraces
	) { ; }

	@Builder
	public static record WhitespaceArrayAccess (
		boolean beforeOpeningBracket, 
		boolean afterOpeningBracket, 
		boolean beforeClosingBracket
	) { ; }

	@Builder
	public static record WhitespaceParameterizedType (
		WhitespaceParameterizedTypeReference reference,
		WhitespaceParameterizedTypeArguments arguments,
		WhitespaceParameterizedTypeParameters parameters,
		WhitespaceParameterizedTypeWildcard wildcard
	) { ; }

	@Builder
	public static record WhitespaceParameterizedTypeReference (
		boolean beforeOpeningAngleBracket, 
		boolean afterOpeningAngleBracket, 
		boolean beforeComma, 
		boolean afterComma, 
		boolean beforeClosingAngleBracket
	) { ; }

	@Builder
	public static record WhitespaceParameterizedTypeArguments (
		boolean beforeOpeningAngleBracket, 
		boolean afterOpeningAngleBracket, 
		boolean beforeComma, 
		boolean afterComma, 
		boolean beforeClosingAngleBracket, 
		boolean afterClosingAngleBracket
	) { ; }

	@Builder
	public static record WhitespaceParameterizedTypeParameters (
		boolean beforeOpeningAngleBracket, 
		boolean afterOpeningAngleBracket, 
		boolean beforeComma, 
		boolean afterComma, 
		boolean beforeClosingAngleBracket, 
		boolean afterClosingAngleBracket, 
		boolean beforeAndInTypeBounds, 
		boolean afterAndInTypeBounds
	) { ; }

	@Builder
	public static record WhitespaceParameterizedTypeWildcard (
		boolean beforeQuestionMark, 
		boolean afterQuestionMark
	) { ; }

	@Builder
	public static record BlankLines (
		int numberOfEmptyLinesToPreserve,
		BlankLinesWithinCompilationUnit withinCompilationUnit,
		BlankLinesWithinTypeDeclarations withinTypeDeclarations,
		BlankLinesWithinMethodOrConstructorDeclarations withinMethodOrConstructorDeclarations
	) { ; }

	@Builder
	public static record BlankLinesWithinCompilationUnit (
		int beforePackage, 
		int afterPackage, 
		int beforeImports, 
		int betweenImportGroups, 
		int afterImports, 
		int betweenClassDeclarations
	) { ; }

	@Builder
	public static record BlankLinesWithinTypeDeclarations (
		int beforeFirstMemberDeclaration, 
		int afterLastMemberDeclaration, 
		int betweenMembersDeclarationsOfDifferentKinds, 
		int betweenMemberTypeDeclarations, 
		int betweenFieldDeclarations, 
		int betweenAbstractMethodDeclarations, 
		int betweenMethodAndConstructorDeclarations
	) { ; }

	@Builder
	public static record BlankLinesWithinMethodOrConstructorDeclarations (
		int atBeginningOfBody, 
		int atEndOfBody, 
		int atBeginningOfCodeBlock, 
		int atEndOfCodeBlock, 
		int beforeStatementWithCodeBlock, 
		int afterStatementWithCodeBlock, 
		int betweenStatementGroupsInSwitch
	) { ; }

	@Builder
	public static record NewLines (
		boolean beforeEmptyStatement, 
		boolean afterOpeningBraceOfArrayInitializer, 
		boolean beforeClosingBraceOfArrayInitializer, 
		boolean atEndOfFile, 
		NewLinesControlStatement controlStatement, 
		NewLinesAfterAnnotation afterAnnotation,
		NewLinesBracedCodeOnOneLine bracedCodeOnOneLine
	) { ; }

	@Builder
	public static record NewLinesControlStatement (
		boolean afterLabels, 
		NewLinesIfElseStatement ifElseStatement, 
		NewLinesLoopStatement loopStatement, 
		NewLinesTryBlock tryBlock
	) { ; }

	@Builder
	public static record NewLinesIfElseStatement (
		boolean beforeElse, 
		boolean keepThenOnSameLine, 
		boolean keepSimpleIfOnOneLine, 
		boolean keepElseOnSameLine, 
		boolean keepElseIfOnOneLine		
	) { ; }

	@Builder
	public static record NewLinesLoopStatement (
		boolean beforeWhileInDo, 
		boolean keepSimpleForOnOneLine, 
		boolean keepSimpleWhileOnOneLine, 
		boolean keepSimpleDoWhileOnOneLine
	) { ; }

	@Builder
	public static record NewLinesTryBlock (
		boolean beforeCatch, 
		boolean beforeFinally
	) { ; }

	@Builder
	public static record NewLinesAfterAnnotation (
		boolean onPackage, 
		boolean onType, 
		boolean onEnumConstant, 
		boolean onField, 
		boolean onMethod, 
		boolean onLocalVariable, 
		boolean onParameter, 
		boolean typeAnnotation
	) { ; }

	@Builder
	public static record NewLinesBracedCodeOnOneLine (
		BracedCodeOnOneLineValue loopBody, 
		BracedCodeOnOneLineValue ifThenStatementBody, 
		boolean keepReturnOrThrowOnOneLine, 
		BracedCodeOnOneLineValue lambdaBody, 
		BracedCodeOnOneLineValue switchCaseWithArrow, 
		BracedCodeOnOneLineValue switchExpressionWithArrow, 
		BracedCodeOnOneLineValue otherCodeBlocksInStatements, 
		BracedCodeOnOneLineValue methodDeclaration, 
		boolean keepSimpleGettersAndSettersOnOneLine, 
		BracedCodeOnOneLineValue classDeclaration, 
		BracedCodeOnOneLineValue anonymousClassDeclaration, 
		BracedCodeOnOneLineValue enumDeclaration, 
		BracedCodeOnOneLineValue enumConstantDeclaration, 
		BracedCodeOnOneLineValue recordDeclaration, 
		BracedCodeOnOneLineValue recordConstructorDeclaration, 
		BracedCodeOnOneLineValue annotationDeclaration
	) { ; }

	@Builder
	public static record LineWrap (
		int maximumLineWidth, 
		int defaultIndentationForWrappedLines, 
		int defaultIndentationForArrayInitializers, 
		boolean neverJoinAlreadyWrappedLines, 
		boolean preferWrappingOuterExpressions, 
		LineWrappingSettings wrapSettings
	) { ; }

	public static record DeclarationWrapping ( LineWrappingPolicy lineWrappingPolicy, boolean forceSplit, IndentationPolicy indentationPolicy ) { }
	public static record InlineDeclarationWrapping ( WrapOperatorPolicy wrapOperatorPolicy, LineWrappingPolicy lineWrappingPolicy, boolean forceSplit, IndentationPolicy indentationPolicy ) { }
	public static record AnnotationWrapping ( LineWrappingPolicy lineWrappingPolicy, boolean forceSplit ) { }

	@Builder
	public static record LineWrappingSettings (
		LineWrapClassDeclaration classDeclaration, 
		LineWrapConstructorDeclaration constructorDeclaration, 
		LineWrapMethodDeclaration methodDeclaration, 
		LineWrapEnumDeclaration enumDeclaration, 
		LineWrapRecordDeclaration recordDeclaration,
		LineWrapFunctionCall functionCall, 
		LineWrapBinaryExpression binaryExpression, 
		LineWrapOtherExpression otherExpression, 
		LineWrapStatement statement, 
		LineWrapParameterizedType parameterizedType, 
		LineWrapAnnotation annotation, 
		LineWrapModuleDescription moduleDescription
	) { ; }
	
	@Builder
	public static record LineWrapClassDeclaration (
		DeclarationWrapping extendsClause, 
		DeclarationWrapping implementsClause, 
		DeclarationWrapping permitsClause
	) { ; }

	@Builder
	public static record LineWrapConstructorDeclaration (
		DeclarationWrapping parameters, 
		DeclarationWrapping throwsClause
	) { ; }

	@Builder
	public static record LineWrapMethodDeclaration (
		DeclarationWrapping declaration, 
		DeclarationWrapping parameters, 
		DeclarationWrapping throwsClause
	) { ; }

	@Builder
	public static record LineWrapEnumDeclaration (
		DeclarationWrapping constant, 
		DeclarationWrapping implementsClause, 
		DeclarationWrapping constantArguments
	) { ; }

	@Builder
	public static record LineWrapRecordDeclaration (
		DeclarationWrapping component, 
		DeclarationWrapping implementsClause
	) { ; }

	@Builder
	public static record LineWrapFunctionCall (
		DeclarationWrapping arguments, 
		DeclarationWrapping qualifiedInvocations, 
		boolean indentFromBaseExpressionFirstLine, 
		DeclarationWrapping explicitConstructorInvocation, 
		DeclarationWrapping objectAllocationArguments, 
		DeclarationWrapping qualifiedObjectAllocationArguments
	) { ; }

	@Builder
	public static record LineWrapBinaryExpression (
		InlineDeclarationWrapping multiplicativeOperator, 
		InlineDeclarationWrapping additiveOperator, 
		InlineDeclarationWrapping stringOperator, 
		InlineDeclarationWrapping shiftOperator, 
		InlineDeclarationWrapping relationalOperator, 
		InlineDeclarationWrapping bitwiseOperator, 
		InlineDeclarationWrapping logicalOperator
	) { ; }

	@Builder
	public static record LineWrapOtherExpression (
		InlineDeclarationWrapping conditionalOperator, 
		DeclarationWrapping chainedConditional, 
		InlineDeclarationWrapping assignment, 
		DeclarationWrapping arrayInitializer
	) { ; }

	@Builder
	public static record LineWrapStatement (
		DeclarationWrapping forStatement, 
		DeclarationWrapping compactIfElse, 
		DeclarationWrapping compactLoop, 
		DeclarationWrapping tryWithResources, 
		InlineDeclarationWrapping multiCatch, 
		InlineDeclarationWrapping switchCaseWithArrow, 
		DeclarationWrapping expressionsInSwitchCaseWithArrow, 
		DeclarationWrapping expressionsInSwitchCaseWithColon, 
		InlineDeclarationWrapping assertMessages
	) { ; }

	@Builder
	public static record LineWrapParameterizedType (
		DeclarationWrapping reference, 
		DeclarationWrapping arguments, 
		DeclarationWrapping parameters
	) { ; }

	@Builder
	public static record LineWrapAnnotation (
		AnnotationWrapping onPackage, 
		AnnotationWrapping onType, 
		AnnotationWrapping onEnumConstant, 
		AnnotationWrapping onField, 
		AnnotationWrapping onMethod, 
		AnnotationWrapping onLocalVariable, 
		AnnotationWrapping onParameters, 
		AnnotationWrapping typeAnnotation, 
		DeclarationWrapping elementValuePairs
	) { ; }

	@Builder
	public static record LineWrapModuleDescription (
		DeclarationWrapping statement
	) { ; }

	@Builder
	public static record Comments (
		int maximumLineWidth, 
		boolean countWidthFromCommentStartingPosition, 
		boolean enableJavadocCommentFormatting, 
		boolean enableBlockCommentFormatting, 
		boolean enableLineCommentFormatting, 
		boolean formatLineCommentsOnFirstColumn, 
		boolean enableHeaderCommentFormatting, 
		boolean preserveWhitespaceBetweenCodeAndLineComments, 
		boolean neverIndentLineCommentsOnFirstColumn, 
		boolean neverIndentBlockCommentsOnFirstColumn, 
		boolean neverJoinLines, 
		Javadocs javadocs, 
		BlockComment blockComment
	) { ; }

	@Builder
	public static record Javadocs (
		boolean formatHtmlTags, 
		boolean doNotPutBlockTagsOnSeparateLines, 
		boolean formatJavaCodeSnippetsInPreTags, 
		boolean blankLinesBeforeJavadocTags, 
		boolean blankLinesBetweenTagsOfDifferentType, 
		AlignJavadocTagsInColumns alignJavadocTagsInColumns, 
		boolean newLineBeforeParam, 
		boolean newLineBeforeThrows, 
		boolean indentWrappedParam, 
		boolean indentWrappedThrows, 
		boolean indentOtherTagDescriptionsWhenWrapped, 
		boolean startAndStopTagsOnDifferentLines, 
		boolean removeBlankLines
	) { ; }

	@Builder
	public static record BlockComment (
		boolean startAndStopTagsOnDifferentLines, 
		boolean removeBlankLines
	) { ; }
}
