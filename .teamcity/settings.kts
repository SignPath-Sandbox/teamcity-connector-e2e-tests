import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script

version = "2024.12"

project {
  buildType {
    name = "Build and Sign"
    id("build_and_sign")

    vcs {
      root(DslContext.settingsRoot)
      cleanCheckout = true
    }

    steps {
      // build step
      script {
        name = "Create artifact"
        scriptContent = "./build.sh"
        formatStderrAsError = true
      }

      // sign step
      step {
        val inputArtifactPath = "BuildOutput"
        val outputArtifactPath = "BuildOutput.zip"

        type = "SignPathRunner"
        param("connectorUrl", "https://teamcity-connector-stable.customersimulation.int.signpath.io")
        param("organizationId", "9ff791fc-c563-44e3-ab8c-86a33c910bbe")
        param("apiToken", "credentialsJSON:a03ec855-c92c-4f33-8877-b8ab1726afd4")
        param("projectSlug", "TeamCity_Connector_E2E_Tests")
        param("signingPolicySlug", "test-signing")
        param("artifactConfigurationSlug", "initial")

        param("inputArtifactPath", inputArtifactPath)
        param("outputArtifactPath", outputArtifactPath)
        param("waitForCompletion", "true")
      }
    }

    // publish the signed artifact
    artifactRules = "BuildOutput.signed.zip"

    // max build duration 5 mins
    failureConditions {
      executionTimeoutMin = 5
    }
  }
}