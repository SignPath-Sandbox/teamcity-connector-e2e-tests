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

    val artifactPath = "BuildOutput.zip"

    // publish the signed artifact
    artifactRules = artifactPath

    steps {
      // build step
      script {
        name = "Create artifact"
        scriptContent = "./build.sh"
        formatStderrAsError = true
      }

      // sign step
      step {
        type = "SignPathRunner"
        param("connectorUrl", "https://teamcity-connector-stable.customersimulation.int.signpath.io")
        param("organizationId", "9ff791fc-c563-44e3-ab8c-86a33c910bbe")
        param("apiToken", "credentialsJSON:a03ec855-c92c-4f33-8877-b8ab1726afd4")
        param("projectSlug", "TeamCity_Connector_E2E_Tests")
        param("signingPolicySlug", "test-signing")
        param("artifactConfigurationSlug", "initial")

        param("inputArtifactPath", artifactPath)
        param("outputArtifactPath", artifactPath)
        param("waitForCompletion", "true")
      }
    }
    
    // max build duration 5 mins
    failureConditions {
      executionTimeoutMin = 5
    }
  }
}