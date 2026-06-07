#!/bin/bash

FEATURE=$1

BASE="/Users/prakashirom/AndroidStudioProjects/mfo-android-cmp-loan-manager/composeApp/src/commonMain/kotlin/com/muthootfincorpone/loanmanager/features/$FEATURE"

# DATA
mkdir -p $BASE/data/apiService
mkdir -p $BASE/data/di
mkdir -p $BASE/data/model
mkdir -p $BASE/data/repository

# DOMAIN
mkdir -p $BASE/domain/di
mkdir -p $BASE/domain/repository
mkdir -p $BASE/domain/usecases

# PRESENTATION
mkdir -p $BASE/presentation/di
mkdir -p $BASE/presentation/ui
mkdir -p $BASE/presentation/viewmodel

echo "✅ Feature '$FEATURE' created successfully!"

# Command to create folder ./create_feature.sh